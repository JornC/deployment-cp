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

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.HashUtil;

public abstract class CompositionJob implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(CompositionJob.class);

  protected final CompositionInfo info;

  protected final Map<Long, List<CompositionInfo>> compositionUpdates;
  protected final ConcurrentMap<String, CompositionInfo> compositions;

  protected final Map<Long, List<ServiceInfo>> serviceUpdates;
  protected final ConcurrentMap<String, ServiceInfo> services;

  protected final ApplicationConfiguration cfg;

  protected final String prId;

  protected final Map<String, String> globalReplacements;

  public CompositionJob(final ApplicationConfiguration cfg, final CompositionInfo info, final String prId,
      final Map<Long, List<CompositionInfo>> compositionUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, CompositionInfo> compositions, final ConcurrentMap<String, ServiceInfo> services) {
    this.cfg = cfg;
    this.info = info;
    this.prId = prId;
    this.compositionUpdates = compositionUpdates;
    this.serviceUpdates = serviceUpdates;
    this.compositions = compositions;
    this.services = services;

    globalReplacements = cfg.getControlPanelProperties().collect(Collectors.toMap(formatKey(), v -> (String) v.getValue()));
    globalReplacements.put("{{cp.pr.id}}", prId);
    globalReplacements.put("{{cp.pr.hash}}", info.hash());

    putComposition(info.busy(true));
  }

  private Function<Entry<Object, Object>, String> formatKey() {
    return v -> String.format("{{%s}}", v.getKey());
  }

  protected void putService(final ServiceInfo service) {
    synchronized (services) {
      services.put(service.hash(), service);

      info.services().stream().filter(v -> v.hash().equals(service.hash())).findFirst().ifPresent(v -> {
        v.status(service.status());
        updateComposition(info);
      });
    }

    updateService(service);
  }

  protected void putComposition(final CompositionInfo composition) {
    synchronized (compositions) {
      compositions.put(composition.hash(), composition);
    }

    updateComposition(composition);
  }

  private void updateComposition(final CompositionInfo info) {
    synchronized (compositionUpdates) {
      compositionUpdates.get(System.currentTimeMillis()).add(info);
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
