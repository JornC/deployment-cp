package nl.yogh.aerius.server.worker.jobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.CompositionStatus;
import nl.yogh.aerius.builder.domain.CompositionType;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.Files;
import nl.yogh.aerius.server.util.HashUtil;

public class ProjectDeploymentJob extends CompositionJob {
  private static final Logger LOG = LoggerFactory.getLogger(ProjectDeploymentJob.class);

  public ProjectDeploymentJob(final ApplicationConfiguration cfg, final CompositionInfo info, final String prId,
      final Map<Long, List<CompositionInfo>> productUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates, final ConcurrentMap<String, CompositionInfo> products,
      final ConcurrentMap<String, ServiceInfo> services) {
    super(cfg, info, prId, productUpdates, serviceUpdates, products, services);
  }

  @Override
  public void run() {
    LOG.info("Deployment job started:  {}", info.hash());

    putComposition(deployProject(prId, info));
    LOG.info("Deployment job complete:  {}", info.hash());
  }

  private CompositionInfo deployProject(final String prId, final CompositionInfo info) {
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

    return info.status(success ? CompositionStatus.DEPLOYED : CompositionStatus.UNBUILT).busy(false);
  }

  private void moveStagingDirectory(final File dir, final CompositionType type) {
    moveStagingDirectory(dir, String.format("%s/projects/%s/", cfg.getStagingDir(), type.name()));
  }

  private boolean deploy(final File dir) {
    try {
      cmd(dir, "./deploy.sh");
      return true;
    } catch (final ProcessExitException e) {
      LOG.debug("Error during deployment: " + e.getOutput().get(0), e);
    } catch (IOException | InterruptedException e) {
      LOG.debug("Unknown error during deployment.", e);
    }

    return false;
  }
}
