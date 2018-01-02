package nl.yogh.aerius.server.worker.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ProjectType;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.domain.ServiceType;
import nl.yogh.aerius.builder.domain.ShallowServiceInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.Files;
import nl.yogh.aerius.server.util.HashUtil;

public class GenericCompilationJob extends ProjectJob {
  private static final Logger LOG = LoggerFactory.getLogger(GenericCompilationJob.class);

  private final Map<String, String> globalReplacements = new HashMap<>();

  public GenericCompilationJob(final ApplicationConfiguration cfg, final String idx, final ProjectInfo info,
      final Map<Long, List<ProjectInfo>> projectUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, ProjectInfo> projects, final ConcurrentMap<String, ServiceInfo> services) {
    super(cfg, info, projectUpdates, serviceUpdates, projects, services);
    putProject(info.busy(true));

    globalReplacements.put("{{pr.id}}", idx);
    globalReplacements.put("{{oAuth.token}}", cfg.getGithubOpenAuthToken());
  }

  @Override
  public void run() {
    LOG.debug("Compiling project: {}", HashUtil.shorten(info.hash()));

    final List<ServiceType> exclusions = services.entrySet().stream()
        .filter(v -> info.services().stream().map(vv -> vv.hash()).anyMatch(r -> r.equals(v.getKey()))).map(v -> v.getValue())
        .filter(v -> v.status() != ServiceStatus.UNBUILT).map(v -> v.type()).collect(Collectors.toList());

    LOG.debug("Exclusions: {}", exclusions);

    final List<ShallowServiceInfo> targets = info.services().stream().filter(v -> !exclusions.contains(v.type())).collect(Collectors.toList());

    for (final ShallowServiceInfo service : targets) {
      putService(compileService(service));
    }

    putProject(deployProject(info));
  }

  private ProjectInfo deployProject(final ProjectInfo info) {
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

    return info.status(success ? ProjectStatus.DEPLOYED : ProjectStatus.UNBUILT).busy(success);
  }

  private ServiceInfo compileService(final ShallowServiceInfo service) {
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

    return ServiceInfo.create(service).status(success ? ServiceStatus.BUILT : ServiceStatus.UNBUILT);
  }

  private void replaceOccurrences(final File dir, final Map<String, String> replacements) {
    try {
      for (final Entry<String, String> entry : replacements.entrySet()) {
        cmd(dir, "sed -i -- 's/%s/%s/g' *", entry.getKey(), entry.getValue());
      }
    } catch (IOException | InterruptedException | ProcessExitException e) {
      LOG.trace("Error during r.");
      // eat
    }
  }

  private void moveStagingDirectory(final File dir, final ServiceType type) {
    moveStagingDirectory(dir, String.format("%s/services/%s/", cfg.getStagingDir(), type.name()));
  }

  private void moveStagingDirectory(final File dir, final ProjectType type) {
    moveStagingDirectory(dir, String.format("%s/projects/%s/", cfg.getStagingDir(), type.name()));
  }

  private void moveStagingDirectory(final File dir, final String source) {
    LOG.debug("Copying: {} to {}", cfg.getStagingDir(), dir.getAbsolutePath());
    try {
      cmd(source, "cp -a * %s", dir.getAbsolutePath());
    } catch (IOException | InterruptedException | ProcessExitException e) {
      LOG.trace("Error during copy.");
      // eat
    }
  }

  private boolean install(final File dir) {
    try {
      cmd(dir, "./install.sh");
      return false;
    } catch (final ProcessExitException e) {
      LOG.trace("Error during install: " + e.getOutput().get(0));
    } catch (IOException | InterruptedException e) {
      LOG.trace("Unknown error during install.");
      // eat
    }

    return false;
  }

  private boolean deploy(final File dir) {
    try {
      cmd(dir, "./deploy.sh");
      return false;
    } catch (final ProcessExitException e) {
      LOG.trace("Error during deployment: " + e.getOutput().get(0));
    } catch (IOException | InterruptedException e) {
      LOG.trace("Unknown error during deployment.");
      // eat
    }

    return false;
  }

  private ArrayList<String> cmd(final File dir, final String format, final String... args)
      throws IOException, InterruptedException, ProcessExitException {
    return cmd(dir, String.format(format, (Object[]) args));
  }

  private ArrayList<String> cmd(final File dir, final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmdDebug(dir, cmd);
  }

  private ArrayList<String> cmd(final String dir, final String format, final String... args)
      throws IOException, InterruptedException, ProcessExitException {
    return cmd(dir, String.format(format, (Object[]) args));
  }

  private ArrayList<String> cmd(final String dir, final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmdDebug(dir, cmd);
  }
}
