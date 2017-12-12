package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PullRequestDeploymentFactory {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestDeploymentFactory.class);

  private static PullRequestDeploymentWorker deploymentWorker;

  public static void init(final Properties properties) {
    synchronized (PullRequestDeploymentFactory.class) {
      if (deploymentWorker == null) {
        deploymentWorker = new PullRequestDeploymentWorker();
      }
    }

    LOG.info("PullRequestDeploymentWorker initialized.");
  }

  public static PullRequestDeploymentWorker getInstance() throws IOException {
    if (deploymentWorker == null) {
      throw new IOException("PullRequestDeploymentFactory not initialized. #init must be called first.");
    }

    return deploymentWorker;
  }

  public static void shutdown() {
    if (deploymentWorker != null) {
      deploymentWorker.shutdown();
    }

    LOG.info("PullRequestDeploymentWorker shut down.");
  }
}
