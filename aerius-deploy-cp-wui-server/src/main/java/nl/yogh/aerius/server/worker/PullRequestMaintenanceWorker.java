package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.PullRequestInfo;

public class PullRequestMaintenanceWorker {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMaintenanceWorker.class);

  /**
   * The pull request update interval, in minutes.
   */
  private static final int UPDATE_INTERVAL = 1;

  private final AERIUSGithubHook githubHook;

  private final Map<Integer, PullRequestInfo> pulls = new TreeMap<>(byReverseIdx);

  private final ExecutorService pullRequestUpdateExecutor;

  /**
   * TODO This can be phased away when we've got a notification hook.
   */
  private final ScheduledExecutorService periodicUpdateExecutor;

  private static Comparator<Integer> byReverseIdx = (o1, o2) -> -Integer.compare(o1, o2);
  private static Comparator<PullRequestInfo> byCompleteness = (o1, o2) -> -Boolean.compare(o1.isIncomplete(), o2.isIncomplete());
  private static Comparator<PullRequestInfo> byLastUpdated = (o1, o2) -> Long.compare(o1.lastUpdated(), o2.lastUpdated());

  public PullRequestMaintenanceWorker(final String oAuthToken) {
    pullRequestUpdateExecutor = Executors.newSingleThreadExecutor();

    githubHook = new AERIUSGithubHook(oAuthToken);
    try {
      githubHook.update(pulls);
      schedulePullRequestUpdate(pulls.values());
    } catch (final IOException e) {
      LOG.error("Failed to initialize pull requests.", e);
      throw new RuntimeException("Could not initialize PullRequests", e);
    }

    periodicUpdateExecutor = Executors.newSingleThreadScheduledExecutor();
    periodicUpdateExecutor.scheduleWithFixedDelay(() -> schedulePullRequestUpdate(findEligiblePullSelection()), 0, UPDATE_INTERVAL, TimeUnit.MINUTES);
  }

  private void schedulePullRequestUpdate(final Collection<PullRequestInfo> pulls) {
    for (final PullRequestInfo info : pulls) {
      schedulePullRequestUpdate(info);
    }
  }

  private void schedulePullRequestUpdate(final PullRequestInfo info) {
    if (info == null) {
      LOG.info("Skipping update job window; no jobs required.");
      return;
    }

    pullRequestUpdateExecutor.submit(CatchAllRunnable.wrap(new PullRequestUpdateJob(info, pulls)));
  }

  public void shutdown() {
    pullRequestUpdateExecutor.shutdownNow();
    periodicUpdateExecutor.shutdownNow();
  }

  private PullRequestInfo findEligiblePullSelection() {
    // Find a pull request that isn't busy, the least complete, and the oldest
    return pulls.values().stream().filter(v -> !v.isBusy()).sorted(byCompleteness.thenComparing(byLastUpdated)).findFirst().orElse(null);
  }

  public ArrayList<PullRequestInfo> getPullRequests() {
    return new ArrayList<PullRequestInfo>(pulls.values());
  }

  public static void main(final String[] args) {
    new PullRequestMaintenanceWorker(args[0]);
  }
}
