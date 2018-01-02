package nl.yogh.aerius.server.worker.jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;

public class ProjectDeploymentJob extends MockProjectJob {
  private static final Logger LOG = LoggerFactory.getLogger(ProjectDeploymentJob.class);

  public ProjectDeploymentJob(final ProjectInfo info, final Map<Long, List<ProjectInfo>> productUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates, final ConcurrentMap<String, ProjectInfo> products,
      final ConcurrentMap<String, ServiceInfo> services) {
    super(ProjectStatus.DEPLOYED, ServiceStatus.BUILT, info, productUpdates, serviceUpdates, products, services);

    LOG.info("Deployment job created:  {}", info.hash());
  }

  @Override
  public void run() {
    LOG.info("Deployment job started:  {}", info.hash());

    super.run();

    LOG.info("Deployment job complete:  {}", info.hash());
  }
}
