package nl.yogh.aerius.server.worker.jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.CompositionStatus;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;

public class MockProjectJob extends CompositionJob {
  private final CompositionStatus projectStatus;
  private final ServiceStatus serviceStatus;

  public MockProjectJob(final CompositionStatus projectStatus, final ServiceStatus serviceStatus, final CompositionInfo info,
      final Map<Long, List<CompositionInfo>> projectUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, CompositionInfo> projects, final ConcurrentMap<String, ServiceInfo> services) {
    super(null, info, null, projectUpdates, serviceUpdates, projects, services);
    this.projectStatus = projectStatus;
    this.serviceStatus = serviceStatus;

    putComposition(info.busy(true));
  }

  @Override
  public void run() {
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }

    for (final ServiceInfo service : info.services()) {
      putService(ServiceInfo.create().hash(service.hash()).status(serviceStatus));
    }

    putComposition(info.busy(false).status(projectStatus));
  }
}