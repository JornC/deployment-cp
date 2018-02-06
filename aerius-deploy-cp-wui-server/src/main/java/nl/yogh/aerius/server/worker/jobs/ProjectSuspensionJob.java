package nl.yogh.aerius.server.worker.jobs;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;

public class ProjectSuspensionJob extends ProjectJob {
  private static final Logger LOG = LoggerFactory.getLogger(ProjectSuspensionJob.class);

  public ProjectSuspensionJob(final ApplicationConfiguration cfg, final ProjectInfo info, final String prId,
      final Map<Long, List<ProjectInfo>> productUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, ProjectInfo> products, final ConcurrentMap<String, ServiceInfo> services) {
    super(cfg, info, prId, productUpdates, serviceUpdates, products, services);
  }

  @Override
  public void run() {
    LOG.info("Suspension job started:  {}", info.hash());

    try {
      cmd("docker ps --filter status=running --format {{.Names}} | grep %s%s%s | cut -d' ' -f1 | xargs docker stop",
          info.type().name().toLowerCase(), prId, info.hash().toLowerCase());
    } catch (IOException | InterruptedException | ProcessExitException e) {
      LOG.error("Failure while suspending..", e);
    }

    LOG.info("Suspension job complete:  {}", info.hash());

    putProject(info.busy(false).status(ProjectStatus.SUSPENDED));
  }
}
