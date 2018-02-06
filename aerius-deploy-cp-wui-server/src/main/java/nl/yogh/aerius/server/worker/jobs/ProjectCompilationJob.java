package nl.yogh.aerius.server.worker.jobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.domain.ServiceType;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.Files;
import nl.yogh.aerius.server.util.HashUtil;

public class ProjectCompilationJob extends ProjectJob {
  private static final Logger LOG = LoggerFactory.getLogger(ProjectCompilationJob.class);

  private final ExecutorService executor = Executors.newFixedThreadPool(1);

  public ProjectCompilationJob(final ApplicationConfiguration cfg, final ProjectInfo info, final String prId,
      final Map<Long, List<ProjectInfo>> projectUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, ProjectInfo> projects, final ConcurrentMap<String, ServiceInfo> services) {
    super(cfg, info, prId, projectUpdates, serviceUpdates, projects, services);
  }

  @Override
  public void run() {
    LOG.info("Compiling project: {} {}", info.type(), HashUtil.shorten(info.hash()));

    final List<ServiceType> exclusions = services.entrySet().stream()
        .filter(v -> info.services().stream().map(vv -> vv.hash()).anyMatch(r -> r.equals(v.getKey()))).map(v -> v.getValue())
        .filter(v -> v.status() != ServiceStatus.UNBUILT).map(v -> v.type()).collect(Collectors.toList());

    LOG.info("Service compilation exclusions: {}", exclusions);

    final List<ServiceInfo> targets = info.services().stream().filter(v -> !exclusions.contains(v.type())).collect(Collectors.toList());

    final CountDownLatch latch = new CountDownLatch(targets.size());

    for (final ServiceInfo service : targets) {
      executor.submit(() -> {
        LOG.info("Compiling service: {} {}", service.type(), HashUtil.shorten(service.hash()));
        try {
          final ServiceInfo resultInfo = compileService(service);
          putService(resultInfo);
        } catch (final Exception e) {
          LOG.error("Exception while compiling service..");
        }

        latch.countDown();
      });
    }

    try {
      latch.await();
    } catch (final InterruptedException e) {
      LOG.error("Exception while waiting for latch.", e);
      return;
    }

    info.status(ProjectStatus.SUSPENDED);

    putProject(info);

    final ProjectDeploymentJob chainJob = new ProjectDeploymentJob(cfg, info, prId, projectUpdates, serviceUpdates, projects, services);
    chainJob.run();
  }

  private ServiceInfo compileService(final ServiceInfo service) {
    final File tmpDir = Files.createTempDir();

    final Map<String, String> localReplacements = new HashMap<>();
    localReplacements.put("{{service.local.hash}}", service.hash());

    // First, copy the staging dir to a temporary dir
    moveStagingDirectory(tmpDir, service.type());

    // Next, replace all replacement markers
    replaceOccurrences(tmpDir, globalReplacements);
    replaceOccurrences(tmpDir, localReplacements);

    // Finally, run the install script
    final boolean success = install(tmpDir);

    LOG.info("Service compilation result: {} for {} ({})", success ? "SUCCESS" : "FAIL", service.type(), HashUtil.shorten(service.hash()));

    return ServiceInfo.create(service).status(success ? ServiceStatus.BUILT : ServiceStatus.UNBUILT);
  }

  private void moveStagingDirectory(final File dir, final ServiceType type) {
    moveStagingDirectory(dir, String.format("%s/services/%s/", cfg.getStagingDir(), type.name()));
  }

  private boolean install(final File dir) {
    try {
      cmd(dir, "./install.sh");
      return true;
    } catch (final ProcessExitException e) {
      LOG.debug("Error during install: " + e.getOutput().get(0), e);
    } catch (IOException | InterruptedException e) {
      LOG.debug("Unknown error during install.", e);
    }

    return false;
  }
}
