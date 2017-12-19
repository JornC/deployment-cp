package nl.yogh.aerius.server.worker.jobs;

import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.collections.MultiMap;

public abstract class ProductJob implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(ProductJob.class);

  protected final ProductInfo info;

  protected final MultiMap<Long, ProductInfo> productUpdates;
  private final ConcurrentMap<String, ProductInfo> products;

  private final MultiMap<Long, ServiceInfo> serviceUpdates;
  protected final ConcurrentMap<String, ServiceInfo> services;

  public ProductJob(final ProductInfo info, final MultiMap<Long, ProductInfo> productUpdates, final MultiMap<Long, ServiceInfo> serviceUpdates,
      final ConcurrentMap<String, ProductInfo> products, final ConcurrentMap<String, ServiceInfo> services) {
    this.info = info;
    this.productUpdates = productUpdates;
    this.serviceUpdates = serviceUpdates;
    this.products = products;
    this.services = services;
  }

  protected void putService(final ServiceInfo service) {
    synchronized (services) {
      services.put(service.hash(), service);
      updateService(service);
    }
  }

  protected void putProduct(final ProductInfo product) {
    synchronized (products) {
      products.put(product.hash(), product);
      updateProduct(product);
    }
  }

  private void updateProduct(final ProductInfo info) {
    info.busy(false);

    synchronized (productUpdates) {
      productUpdates.get(System.currentTimeMillis()).add(info);
    }

    LOG.info("Product updated. {} -> {}", info.hash(), info.status());
  }

  private void updateService(final ServiceInfo info) {
    synchronized (serviceUpdates) {
      serviceUpdates.get(System.currentTimeMillis()).add(info);
    }

    LOG.info("Service updated. {} -> {}", info.hash(), info.status());
  }
}
