package nl.yogh.aerius.server.worker.jobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.HashUtil;

public abstract class ProjectJob implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(ProjectJob.class);

  protected final ProjectInfo info;

  protected final Map<Long, List<ProjectInfo>> projectUpdates;
  protected final ConcurrentMap<String, ProjectInfo> projects;

  protected final Map<Long, List<ServiceInfo>> serviceUpdates;
  protected final ConcurrentMap<String, ServiceInfo> services;

  protected final ApplicationConfiguration cfg;

  protected final String prId;

  protected final Map<String, String> globalReplacements;

  public ProjectJob(final ApplicationConfiguration cfg, final ProjectInfo info, final String prId, final Map<Long, List<ProjectInfo>> projectUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, ProjectInfo> projects, final ConcurrentMap<String, ServiceInfo> services) {
    this.cfg = cfg;
    this.info = info;
    this.prId = prId;
    this.projectUpdates = projectUpdates;
    this.serviceUpdates = serviceUpdates;
    this.projects = projects;
    this.services = services;

    globalReplacements = cfg.getControlPanelProperties().collect(Collectors.toMap(formatKey(), v -> (String) v.getValue()));
    globalReplacements.put("{{cp.pr.id}}", prId);
    globalReplacements.put("{{cp.pr.hash}}", info.hash());

    putProject(info.busy(true));
  }

  private Function<Entry<Object, Object>, String> formatKey() {
    return v -> String.format("{{%s}}", v.getKey());
  }

  protected void putService(final ServiceInfo service) {
    synchronized (services) {
      services.put(service.hash(), service);

      info.services().stream().filter(v -> v.hash().equals(service.hash())).findFirst().ifPresent(v -> {
        v.status(service.status());
        updateProject(info);
      });
    }

    updateService(service);
  }

  protected void putProject(final ProjectInfo product) {
    synchronized (projects) {
      projects.put(product.hash(), product);
    }

    updateProject(product);
  }

  private void updateProject(final ProjectInfo info) {
    synchronized (projectUpdates) {
      projectUpdates.get(System.currentTimeMillis()).add(info);
    }

    LOG.debug("Product updated. {} {} -> {}", info.type(), HashUtil.shorten(info.hash()), info.status());
  }

  private void updateService(final ServiceInfo info) {
    synchronized (serviceUpdates) {
      serviceUpdates.get(System.currentTimeMillis()).add(info);
    }

    LOG.debug("Service updated. {} {} -> {}", info.type(), HashUtil.shorten(info.hash()), info.status());
  }

  protected void replaceOccurrences(final File dir, final Map<String, String> replacements) {
    for (final Entry<String, String> entry : replacements.entrySet()) {
      try {
        cmd(dir, "sed -i -- 's/%s/%s/g' *", entry.getKey(), escape(entry.getValue()));
      } catch (IOException | InterruptedException | ProcessExitException e) {
        LOG.trace("Error during r.");
        // eat
      }
    }
  }

  private String escape(final String value) {
    return value.replace("/", "\\/");
  }

  protected void moveStagingDirectory(final File dir, final String source) {
    LOG.debug("Copying: {} to {}", cfg.getStagingDir(), dir.getAbsolutePath());
    try {
      cmd(source, "cp -a * %s", dir.getAbsolutePath());
    } catch (IOException | InterruptedException | ProcessExitException e) {
      LOG.trace("Error during copy.");
      // eat
    }
  }

  protected ArrayList<String> cmd(final File dir, final String format, final String... args)
      throws IOException, InterruptedException, ProcessExitException {
    return cmd(dir, String.format(format, (Object[]) args));
  }

  protected ArrayList<String> cmd(final File dir, final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmd(dir, cmd);
  }

  protected ArrayList<String> cmd(final String dir, final String format, final String... args)
      throws IOException, InterruptedException, ProcessExitException {
    return cmd(dir, String.format(format, (Object[]) args));
  }

  protected ArrayList<String> cmd(final String dir, final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmd(dir, cmd);
  }

  protected ArrayList<String> cmdDebug(final File dir, final String format, final String... args)
      throws IOException, InterruptedException, ProcessExitException {
    return cmdDebug(dir, String.format(format, (Object[]) args));
  }

  protected ArrayList<String> cmdDebug(final File dir, final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmdDebug(dir, cmd);
  }

  protected ArrayList<String> cmdDebug(final String dir, final String format, final String... args)
      throws IOException, InterruptedException, ProcessExitException {
    return cmdDebug(dir, String.format(format, (Object[]) args));
  }

  protected ArrayList<String> cmdDebug(final String dir, final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmdDebug(dir, cmd);
  }
}
