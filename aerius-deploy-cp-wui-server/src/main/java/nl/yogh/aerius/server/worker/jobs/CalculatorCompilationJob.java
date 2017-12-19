package nl.yogh.aerius.server.worker.jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;

public class CalculatorCompilationJob extends ProjectJob {
  public CalculatorCompilationJob(final String idx, final ProjectInfo info, final Map<Long, List<ProjectInfo>> projectUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates, final ConcurrentMap<String, ProjectInfo> projects,
      final ConcurrentMap<String, ServiceInfo> services) {
    super(info, projectUpdates, serviceUpdates, projects, services);
  }

  @Override
  public void run() {
    updateProject(info.busy(false));
  }
}
