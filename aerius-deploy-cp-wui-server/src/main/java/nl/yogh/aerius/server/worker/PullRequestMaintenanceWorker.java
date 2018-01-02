package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.worker.jobs.CatchAllRunnable;
import nl.yogh.aerius.server.worker.jobs.PullRequestUpdateJob;

public class PullRequestMaintenanceWorker {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMaintenanceWorker.class);

  /**
   * The pull request update interval, in minutes.
   */
  private static final int UPDATE_INTERVAL = 15;
  /**
   * Project update interval, in minutes.
   */
  private static final int PROJECT_UPDATE_INTERVAL = 15;

  private final AERIUSGithubHook githubHook;

  private final ConcurrentMap<Integer, PullRequestInfo> pulls = new ConcurrentHashMap<>();

  private final ExecutorService pullRequestLocalUpdateExecutor;

  /**
   * TODO This can be phased away when we've got a notification hook.
   */
  private final ScheduledExecutorService pullRequestUpdateExecutor;

  private final ScheduledExecutorService projectUpdateExecutor;

  private static Comparator<PullRequestInfo> byReverseIdx = (a, b) -> -Integer.compare(Integer.parseInt(a.idx()), Integer.parseInt(b.idx()));

  private long lastProjectUpdate;

  private final ApplicationConfiguration cfg;

  public PullRequestMaintenanceWorker(final ApplicationConfiguration cfg, final TimestampedMultiMap<ProjectInfo> projectUpdates) {
    this.cfg = cfg;
    pullRequestLocalUpdateExecutor = Executors.newSingleThreadExecutor();

    githubHook = new AERIUSGithubHook(cfg.getGithubOpenAuthToken());

    pullRequestUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    pullRequestUpdateExecutor.scheduleWithFixedDelay(() -> updatePullRequestsFromGithub(), 0, UPDATE_INTERVAL, TimeUnit.MINUTES);

    projectUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    projectUpdateExecutor.scheduleWithFixedDelay(() -> updateProjects(projectUpdates), PROJECT_UPDATE_INTERVAL, PROJECT_UPDATE_INTERVAL,
        TimeUnit.SECONDS);
  }

  private void updateProjects(final TimestampedMultiMap<ProjectInfo> projectUpdates) {
    final ArrayList<ProjectInfo> updates = projectUpdates.getUpdates(lastProjectUpdate);
    if (updates.isEmpty()) {
      return;
    }

    final long startTime = System.currentTimeMillis();

    LOG.debug("Started synchronizing {} projects.", updates.size());

    try {
      for (final ProjectInfo project : updates) {
        for (final PullRequestInfo pull : pulls.values()) {
          if (pull.projects() == null || pull.projects().isEmpty()) {
            continue;
          }

          pull.projects().values().stream().filter(p -> p.hash().equals(project.hash())).forEach(p -> update(p, project));
        }
      }
    } catch (final Exception e) {
      LOG.error("Error while updating a project", e);
    }

    lastProjectUpdate = System.currentTimeMillis();

    LOG.debug("Synchronized {} projects in {}ms.", updates.size(), lastProjectUpdate - startTime);
  }

  private void update(final ProjectInfo target, final ProjectInfo source) {
    target.status(source.status());
    target.busy(source.busy());
  }

  private void updatePullRequestsFromGithub() {
    try {
      githubHook.update(pulls);
      schedulePullRequestUpdate(pulls.values());
    } catch (final IOException e) {
      LOG.error("Failed to initialize pull requests.", e);
      throw new RuntimeException("Could not initialize PullRequests", e);
    }
  }

  private void schedulePullRequestUpdate(final Collection<PullRequestInfo> pulls) {
    pulls.stream().sorted(byReverseIdx).filter(PullRequestInfo::isIncomplete).forEach(v -> schedulePullRequestUpdate(v));
  }

  private void schedulePullRequestUpdate(final PullRequestInfo info) {
    pullRequestLocalUpdateExecutor.submit(CatchAllRunnable.wrap(new PullRequestUpdateJob(cfg, info, pulls)));
  }

  public void shutdown() {
    pullRequestLocalUpdateExecutor.shutdownNow();
    pullRequestUpdateExecutor.shutdownNow();
    projectUpdateExecutor.shutdownNow();
  }

  public ArrayList<PullRequestInfo> getPullRequests() {
    ArrayList<PullRequestInfo> lst;
    lst = new ArrayList<PullRequestInfo>(pulls.values());

    lst.sort(byReverseIdx);
    return lst;
  }

  public long getLastUpdate() {
    return lastProjectUpdate;
  }
}
