package nl.yogh.aerius.server.worker;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.service.ProductDeploymentAction;

public class PullRequestDeploymentWorker {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestDeploymentWorker.class);

  private static final int CACHE_MINUTES = 15;
  private static final long CACHE_MILLISECONDS = CACHE_MINUTES * 60 * 1000;

  private final ScheduledExecutorService clearCacheExecutor;

  private final TreeMap<Long, ProductInfo> updateCache = new TreeMap<>();

  public PullRequestDeploymentWorker() {
    clearCacheExecutor = Executors.newSingleThreadScheduledExecutor();
    clearCacheExecutor.scheduleWithFixedDelay(() -> {
      final long clearBefore = new Date().getTime() - CACHE_MILLISECONDS;
      updateCache.keySet().removeIf(o -> o < clearBefore);
      LOG.info("UpdateCache cleared up to {}", new Date(clearBefore).toString());
    }, 0, CACHE_MINUTES, TimeUnit.MINUTES);
  }

  public static void main(final String[] args) {
    new PullRequestDeploymentWorker();
  }

  public void shutdown() {
    clearCacheExecutor.shutdownNow();
  }

  public ArrayList<ProductInfo> getUpdates(final long since) {
    return updateCache.entrySet().stream().filter(e -> e.getKey() > since).map(e -> e.getValue()).collect(Collectors.toCollection(ArrayList::new));
  }

  public void doAction(final ProductType type, final ProductDeploymentAction action, final ProductInfo info) {

  }
}
