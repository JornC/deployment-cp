package nl.yogh.aerius.server.worker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.service.ProductDeploymentAction;
import nl.yogh.aerius.server.collections.MultiMap;
import nl.yogh.aerius.server.worker.jobs.CatchAllRunnable;
import nl.yogh.aerius.server.worker.jobs.ProductCompilationJob;
import nl.yogh.aerius.server.worker.jobs.ProductDeploymentJob;
import nl.yogh.aerius.server.worker.jobs.ProductSuspensionJob;

public class PullRequestDeploymentWorker {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestDeploymentWorker.class);

  private static final int CACHE_MINUTES = 15;
  private static final long CACHE_MILLISECONDS = CACHE_MINUTES * 60 * 1000;

  private final ExecutorService productCompilationExecutor;
  private final ExecutorService productDeploymentExecutor;
  private final ExecutorService productSuspensionExecutor;

  private final ScheduledExecutorService clearCacheExecutor;

  private final MultiMap<Long, ProductInfo> productUpdates = new MultiMap<>();
  private final MultiMap<Long, ServiceInfo> serviceUpdates = new MultiMap<>();

  private final ConcurrentMap<String, ProductInfo> products = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, ServiceInfo> services = new ConcurrentHashMap<>();

  public PullRequestDeploymentWorker() {
    clearCacheExecutor = Executors.newSingleThreadScheduledExecutor();
    clearCacheExecutor.scheduleWithFixedDelay(() -> {
      final long clearBefore = new Date().getTime() - CACHE_MILLISECONDS;
      productUpdates.keySet().removeIf(o -> o < clearBefore);
      serviceUpdates.keySet().removeIf(o -> o < clearBefore);
      LOG.info("UpdateCache cleared up to {}", new Date(clearBefore).toString());
    }, 0, CACHE_MINUTES, TimeUnit.MINUTES);

    productCompilationExecutor = Executors.newSingleThreadExecutor();
    productDeploymentExecutor = Executors.newFixedThreadPool(2);
    productSuspensionExecutor = Executors.newFixedThreadPool(2);
  }

  public static void main(final String[] args) {
    new PullRequestDeploymentWorker();
  }

  public void shutdown() {
    clearCacheExecutor.shutdownNow();
  }

  public void doAction(final ProductType type, final ProductDeploymentAction action, final ProductInfo info) {
    switch (action) {
    case BUILD:
      productCompilationExecutor.submit(CatchAllRunnable.wrap(new ProductCompilationJob(info, productUpdates, serviceUpdates, products, services)));
      break;
    case SUSPEND:
      productSuspensionExecutor.submit(CatchAllRunnable.wrap(new ProductSuspensionJob(info, productUpdates, serviceUpdates, products, services)));
      break;
    case DEPLOY:
      productDeploymentExecutor.submit(CatchAllRunnable.wrap(new ProductDeploymentJob(info, productUpdates, serviceUpdates, products, services)));
      break;
    case DESTROY:
    default:
      LOG.info("Not implemented yet or unknown: {}", action);
    }
  }

  public ArrayList<ProductInfo> getProductUpdates(final long since) {
    return getUpdate(since, productUpdates);
  }

  public ArrayList<ServiceInfo> getServiceUpdates(final long since) {
    return getUpdate(since, serviceUpdates);
  }

  private <T> ArrayList<T> getUpdate(final long since, final Map<Long, List<T>> map) {
    return map.entrySet().stream().filter(e -> e.getKey() > since).flatMap(e -> e.getValue().stream())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public ArrayList<ProductInfo> getProducts() {
    return new ArrayList<>(products.values());
  }

  public ArrayList<ServiceInfo> getServices() {
    return new ArrayList<>(services.values());
  }
}
