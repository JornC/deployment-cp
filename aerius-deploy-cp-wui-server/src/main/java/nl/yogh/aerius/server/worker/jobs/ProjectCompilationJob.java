package nl.yogh.aerius.server.worker.jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectType;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;

public class ProjectCompilationJob extends ProjectJob {
  private final Runnable delegate;

  public ProjectCompilationJob(final String idx, final ProjectType type, final ProjectInfo info, final Map<Long, List<ProjectInfo>> projectUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates, final ConcurrentMap<String, ProjectInfo> projects,
      final ConcurrentMap<String, ServiceInfo> services) {
    super(info, projectUpdates, serviceUpdates, projects, services);

    switch (type) {
    case CALCULATOR:
      delegate = new CalculatorCompilationJob(idx, info, projectUpdates, serviceUpdates, projects, services);
      break;
    default:
      delegate = new MockProjectJob(ServiceStatus.DEPLOYED, info, projectUpdates, serviceUpdates, projects, services);
      break;
    }

  }

  @Override
  public void run() {
    delegate.run();
  }
}
