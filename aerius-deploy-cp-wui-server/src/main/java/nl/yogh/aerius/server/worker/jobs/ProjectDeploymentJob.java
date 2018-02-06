package nl.yogh.aerius.server.worker.jobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ProjectType;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.Files;
import nl.yogh.aerius.server.util.HashUtil;

public class ProjectDeploymentJob extends ProjectJob {
  private static final Logger LOG = LoggerFactory.getLogger(ProjectDeploymentJob.class);
  private final String prId;

  public ProjectDeploymentJob(final ApplicationConfiguration cfg, final ProjectInfo info, final String prId,
      final Map<Long, List<ProjectInfo>> productUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates, final ConcurrentMap<String, ProjectInfo> products,
      final ConcurrentMap<String, ServiceInfo> services) {
    super(cfg, info, prId, productUpdates, serviceUpdates, products, services);

    this.prId = prId;

    LOG.info("Deployment job created:  {}", info.hash());
  }

  @Override
  public void run() {
    LOG.info("Deployment job started:  {}", info.hash());

    putProject(deployProject(prId, info));

    LOG.info("Deployment job complete:  {}", info.hash());
  }

  private ProjectInfo deployProject(final String prId, final ProjectInfo info) {
    final File tmpDir = Files.createTempDir();

    final Map<String, String> localReplacements = new HashMap<>();
    info.services().forEach(v -> localReplacements.put(String.format("{{service.%s.hash}}", v.type()), v.hash()));
    localReplacements.put("{{project.hash}}", info.hash());

    // First, copy the staging dir to a temporary dir
    moveStagingDirectory(tmpDir, info.type());

    // Next, replace all replacement markers
    replaceOccurrences(tmpDir, globalReplacements);
    replaceOccurrences(tmpDir, localReplacements);

    // Finally, run the deploy script
    final boolean success = deploy(tmpDir);

    LOG.info("Project deployment result: {} for {} ({})", success ? "SUCCESS" : "FAIL", info.type(), HashUtil.shorten(info.hash()));

    if (success) {
      final String url = String.format(cfg.getDeploymentHost(info.type()), prId);
      LOG.info("Deployed project to: {}", url);
      info.url(url);
    }

    return info.status(success ? ProjectStatus.DEPLOYED : ProjectStatus.UNBUILT).busy(false);
  }

  private void moveStagingDirectory(final File dir, final ProjectType type) {
    moveStagingDirectory(dir, String.format("%s/projects/%s/", cfg.getStagingDir(), type.name()));
  }

  private boolean deploy(final File dir) {
    try {
      cmd(dir, "./deploy.sh");
      return true;
    } catch (final ProcessExitException e) {
      LOG.info("Error during deployment: " + e.getOutput().get(0));
    } catch (IOException | InterruptedException e) {
      LOG.trace("Unknown error during deployment.");
      // eat
    }

    return false;
  }
}
