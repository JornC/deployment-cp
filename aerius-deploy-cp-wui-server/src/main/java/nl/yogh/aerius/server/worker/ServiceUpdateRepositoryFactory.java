package nl.yogh.aerius.server.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;

public class ServiceUpdateRepositoryFactory {
  private static final Logger LOG = LoggerFactory.getLogger(ServiceUpdateRepositoryFactory.class);

  private static TimestampedMultiMap<ServiceInfo> serviceUpdateRepository;

  public static void init() {
    synchronized (ServiceUpdateRepositoryFactory.class) {
      if (serviceUpdateRepository == null) {
        serviceUpdateRepository = new TimestampedMultiMap<>();
      }
    }

    LOG.info("ServiceUpdateFactory initialized.");
  }

  public static TimestampedMultiMap<ServiceInfo> getInstance() {
    if (serviceUpdateRepository == null) {
      init();
    }

    return serviceUpdateRepository;
  }
}
