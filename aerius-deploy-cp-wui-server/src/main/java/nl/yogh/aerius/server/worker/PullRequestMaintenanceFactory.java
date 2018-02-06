package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;
import nl.yogh.aerius.server.util.ApplicationConfiguration;

public class PullRequestMaintenanceFactory {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMaintenanceFactory.class);

  private static PullRequestMaintenanceWorker maintenanceWorker;

  public static void init(final ApplicationConfiguration cfg, final ConcurrentMap<String, ProjectInfo> projects,
      final ConcurrentMap<String, ServiceInfo> services, final TimestampedMultiMap<ProjectInfo> projectUpdates) {
    synchronized (PullRequestMaintenanceFactory.class) {
      if (maintenanceWorker == null) {
        maintenanceWorker = new PullRequestMaintenanceWorker(cfg, projects, projectUpdates, services);
      }
    }

    LOG.info("PullRequestMaintenanceWorker initialized.");
  }

  public static PullRequestMaintenanceWorker getInstance() throws IOException {
    if (maintenanceWorker == null) {
      throw new IOException("PullRequestMaintenanceWorker not initialized. #init must be called first.");
    }

    return maintenanceWorker;
  }

  public static void shutdown() {
    if (maintenanceWorker != null) {
      maintenanceWorker.shutdown();
    }

    LOG.info("PullRequestMaintenanceWorker shut down.");
  }
}
