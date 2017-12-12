package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullRequestMaintenanceFactory {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMaintenanceFactory.class);

  private static PullRequestMaintenanceWorker maintenanceWorker;

  public static void init(final Properties properties) {
    synchronized (PullRequestMaintenanceFactory.class) {
      if (maintenanceWorker == null) {
        final String oauthToken = getPropertyRequired(properties, "deployment.cp.oath.token");

        maintenanceWorker = new PullRequestMaintenanceWorker(oauthToken);
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

  private static String getPropertyRequired(final Properties properties, final String key) {
    final String prop = properties.getProperty(key);
    if (prop == null) {
      throw new IllegalStateException("Required property not present: " + key);
    }

    return prop;
  }
}
