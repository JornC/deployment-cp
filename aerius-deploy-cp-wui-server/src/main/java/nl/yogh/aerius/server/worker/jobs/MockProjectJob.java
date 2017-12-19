package nl.yogh.aerius.server.worker.jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;

public class MockProjectJob extends ProjectJob {
  private final ServiceStatus status;

  public MockProjectJob(final ServiceStatus status, final ProjectInfo info, final Map<Long, List<ProjectInfo>> projectUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates, final ConcurrentMap<String, ProjectInfo> projects,
      final ConcurrentMap<String, ServiceInfo> services) {
    super(info, projectUpdates, serviceUpdates, projects, services);

    this.status = status;

    updateProject(info.busy(true));
  }

  @Override
  public void run() {
    try {
      Thread.sleep(1000);
    } catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }

    for (final String service : info.services()) {
      putService(ServiceInfo.create().hash(service).status(status));
    }

    putProject(info.busy(false).status(status));
  }
}