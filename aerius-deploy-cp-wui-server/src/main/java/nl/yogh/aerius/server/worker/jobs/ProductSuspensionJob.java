package nl.yogh.aerius.server.worker.jobs;

import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.server.collections.MultiMap;

public class ProductSuspensionJob extends ProductJob {
  private static final Logger LOG = LoggerFactory.getLogger(ProductSuspensionJob.class);

  public ProductSuspensionJob(final ProductInfo info, final MultiMap<Long, ProductInfo> productUpdates,
      final MultiMap<Long, ServiceInfo> serviceUpdates, final ConcurrentMap<String, ProductInfo> products,
      final ConcurrentMap<String, ServiceInfo> services) {
    super(info, productUpdates, serviceUpdates, products, services);

    LOG.info("Suspension job created:  {}", info.hash());

  }

  @Override
  public void run() {
    LOG.info("Suspension job started:  {}", info.hash());

    info.busy(true);

    try {
      Thread.sleep(5000);
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }

    for (final String service : info.services()) {
      putService(ServiceInfo.create().hash(service).status(ServiceStatus.SUSPENDED));
    }

    info.status(ServiceStatus.SUSPENDED);
    putProduct(info);

    LOG.info("Suspension job complete:  {}", info.hash());
  }
}
