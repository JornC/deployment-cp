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

import nl.yogh.aerius.builder.domain.CommitInfo;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
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

  private final GithubHook githubHook;

  private final ConcurrentMap<String, CommitInfo> builds = new ConcurrentHashMap<>();

  private ExecutorService pullRequestLocalUpdateExecutor;

  /**
   * TODO This can be phased away when we've got a notification hook.
   */
  private final ScheduledExecutorService pullRequestUpdateExecutor;

  private final ScheduledExecutorService projectUpdateExecutor;

  private static Comparator<CommitInfo> byReverseIdx = (a, b) -> {
    try {
      return -Integer.compare(Integer.parseInt(a.idx()), Integer.parseInt(b.idx()));
    } catch (final NumberFormatException e) {
      return String.valueOf(a).compareTo(String.valueOf(b));
    }
  };

  private long lastProjectUpdate;

  private final ApplicationConfiguration cfg;

  private final ConcurrentMap<String, CompositionInfo> projects;

  private final ConcurrentMap<String, ServiceInfo> services;

  public PullRequestMaintenanceWorker(final ApplicationConfiguration cfg, final ConcurrentMap<String, CompositionInfo> projects,
      final TimestampedMultiMap<CompositionInfo> projectUpdates, final ConcurrentMap<String, ServiceInfo> services) {
    this.cfg = cfg;
    this.projects = projects;
    this.services = services;
    pullRequestLocalUpdateExecutor = Executors.newSingleThreadExecutor();

    githubHook = new GithubHook(cfg);

    pullRequestUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    pullRequestUpdateExecutor.scheduleWithFixedDelay(() -> updatePullRequestsFromGithub(), 0, UPDATE_INTERVAL, TimeUnit.MINUTES);

    projectUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    projectUpdateExecutor.scheduleWithFixedDelay(() -> updateProjects(projectUpdates), PROJECT_UPDATE_INTERVAL, PROJECT_UPDATE_INTERVAL,
        TimeUnit.SECONDS);
  }

  private void updateProjects(final TimestampedMultiMap<CompositionInfo> projectUpdates) {
    final ArrayList<CompositionInfo> updates = projectUpdates.getUpdates(lastProjectUpdate);
    if (updates.isEmpty()) {
      return;
    }

    final long startTime = System.currentTimeMillis();

    LOG.debug("Started synchronizing {} projects.", updates.size());

    try {
      for (final CompositionInfo project : updates) {
        for (final CommitInfo pull : builds.values()) {
          if (pull.compositions() == null || pull.compositions().isEmpty()) {
            continue;
          }

          pull.compositions().values().stream().filter(p -> p.hash().equals(project.hash())).forEach(p -> update(p, project));
        }
      }
    } catch (final Exception e) {
      LOG.error("Error while updating a project", e);
    }

    lastProjectUpdate = System.currentTimeMillis();

    LOG.debug("Synchronized {} projects in {}ms.", updates.size(), lastProjectUpdate - startTime);
  }

  private void update(final CompositionInfo target, final CompositionInfo source) {
    target.status(source.status());
    target.busy(source.busy());
  }

  private void updatePullRequestsFromGithub() {
    try {
      githubHook.update(projects, builds);
      schedulePullRequestUpdate(builds.values());
    } catch (final IOException e) {
      LOG.error("Failed to initialize pull requests.", e);
      throw new RuntimeException("Could not initialize PullRequests", e);
    }
  }

  private void schedulePullRequestUpdate(final Collection<CommitInfo> pulls) {
    pulls.stream().sorted(byReverseIdx).filter(CommitInfo::isIncomplete).forEach(v -> schedulePullRequestUpdate(v));
  }

  private void schedulePullRequestUpdate(final CommitInfo info) {
    pullRequestLocalUpdateExecutor.submit(CatchAllRunnable.wrap(new PullRequestUpdateJob(cfg, info, builds, projects, services)));
  }

  public void shutdown() {
    pullRequestLocalUpdateExecutor.shutdownNow();
    pullRequestUpdateExecutor.shutdownNow();
    projectUpdateExecutor.shutdownNow();
  }

  public ArrayList<CommitInfo> getPullRequests() {
    ArrayList<CommitInfo> lst;
    lst = new ArrayList<CommitInfo>(builds.values());

    lst.sort(byReverseIdx);
    return lst;
  }

  public long getLastUpdate() {
    return lastProjectUpdate;
  }

  public void purge() {
    pullRequestLocalUpdateExecutor.shutdownNow();

    builds.clear();
    projects.clear();
    services.clear();

    pullRequestLocalUpdateExecutor = Executors.newSingleThreadExecutor();
    updatePullRequestsFromGithub();
  }
}
