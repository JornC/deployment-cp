package nl.yogh.aerius.server.worker.jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.domain.ShallowServiceInfo;

public class MockProjectJob extends ProjectJob {
  private final ProjectStatus projectStatus;
  private final ServiceStatus serviceStatus;

  public MockProjectJob(final ProjectStatus projectStatus, final ServiceStatus serviceStatus, final ProjectInfo info,
      final Map<Long, List<ProjectInfo>> projectUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, ProjectInfo> projects, final ConcurrentMap<String, ServiceInfo> services) {
    super(null, info, projectUpdates, serviceUpdates, projects, services);
    this.projectStatus = projectStatus;
    this.serviceStatus = serviceStatus;

    putProject(info.busy(true));
  }

  @Override
  public void run() {
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }

    for (final ShallowServiceInfo service : info.services()) {
      putService(ServiceInfo.create().hash(service.hash()).status(serviceStatus));
    }

    putProject(info.busy(false).status(projectStatus));
  }
}