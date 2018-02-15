package nl.yogh.aerius.server.worker.jobs;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.CompositionStatus;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;

public class CompositionSuspensionJob extends CompositionJob {
  private static final Logger LOG = LoggerFactory.getLogger(CompositionSuspensionJob.class);

  public CompositionSuspensionJob(final ApplicationConfiguration cfg, final CompositionInfo info, final String prId,
      final Map<Long, List<CompositionInfo>> productUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, CompositionInfo> products, final ConcurrentMap<String, ServiceInfo> services) {
    super(cfg, info, prId, productUpdates, serviceUpdates, products, services);
  }

  @Override
  public void run() {
    LOG.info("Suspension job started:  {}", info.hash());

    try {
      cmd("/", "docker ps --filter status=running --format {{.Names}} | grep %s%s%s | cut -d' ' -f1 | xargs docker stop",
          info.type().name().toLowerCase(), prId, info.hash().toLowerCase());
    } catch (IOException | InterruptedException | ProcessExitException e) {
      LOG.error("Failure while suspending..", e);
    }

    LOG.info("Suspension job complete:  {}", info.hash());

    putComposition(info.busy(false).status(CompositionStatus.SUSPENDED));
  }
}
