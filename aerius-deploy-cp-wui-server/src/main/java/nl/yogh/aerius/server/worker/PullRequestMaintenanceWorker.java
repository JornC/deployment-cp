package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;

public class PullRequestMaintenanceWorker {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMaintenanceWorker.class);

  /**
   * The pull request update interval, in minutes.
   */
  private static final int UPDATE_INTERVAL = 15;

  private final AERIUSGithubHook githubHook;

  private final Map<Integer, PullRequestInfo> pulls = new TreeMap<>(byReverseIdx);
  private final ConcurrentMap<String, ServiceStatus> services = new ConcurrentHashMap<>();

  private final ExecutorService pullRequestUpdateExecutor;

  /**
   * TODO This can be phased away when we've got a notification hook.
   */
  private final ScheduledExecutorService periodicUpdateExecutor;

  private static Comparator<Integer> byReverseIdx = (o1, o2) -> -Integer.compare(o1, o2);

  private final String baseDir;

  public PullRequestMaintenanceWorker(final String baseDir, final String oAuthToken) {
    this.baseDir = baseDir;
    pullRequestUpdateExecutor = Executors.newSingleThreadExecutor();

    githubHook = new AERIUSGithubHook(oAuthToken);

    periodicUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    periodicUpdateExecutor.scheduleWithFixedDelay(() -> updatePullRequestsFromGithub(), 0, UPDATE_INTERVAL, TimeUnit.MINUTES);
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
    pulls.stream().filter(PullRequestInfo::isIncomplete).forEach(v -> schedulePullRequestUpdate(v));
  }

  private void schedulePullRequestUpdate(final PullRequestInfo info) {
    pullRequestUpdateExecutor.submit(CatchAllRunnable.wrap(new PullRequestUpdateJob(baseDir, info, pulls, services)));
  }

  public void shutdown() {
    pullRequestUpdateExecutor.shutdownNow();
    periodicUpdateExecutor.shutdownNow();
  }

  public ArrayList<PullRequestInfo> getPullRequests() {
    synchronized (pulls) {
      return new ArrayList<PullRequestInfo>(pulls.values());
    }
  }

  public static void main(final String[] args) {
    new PullRequestMaintenanceWorker(args[0], args[1]);
  }
}
