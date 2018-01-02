package nl.yogh.aerius.server.worker.jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.server.util.ApplicationConfiguration;

public class ProjectCompilationJob extends ProjectJob {
  private final Runnable delegate;

  public ProjectCompilationJob(final ApplicationConfiguration cfg, final String idx, final ProjectInfo info,
      final Map<Long, List<ProjectInfo>> projectUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates, final ConcurrentMap<String, ProjectInfo> projects,
      final ConcurrentMap<String, ServiceInfo> services) {
    super(cfg, info, projectUpdates, serviceUpdates, projects, services);

    switch (info.type()) {
    case CALCULATOR:
      delegate = new GenericCompilationJob(cfg, idx, info, projectUpdates, serviceUpdates, projects, services);
      break;
    default:
      delegate = new MockProjectJob(ProjectStatus.SUSPENDED, ServiceStatus.BUILT, info, projectUpdates, serviceUpdates, projects, services);
      break;
    }

  }

  @Override
  public void run() {
    delegate.run();
  }
}
