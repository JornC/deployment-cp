package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;

public class PullRequestDeploymentFactory {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestDeploymentFactory.class);

  private static PullRequestDeploymentWorker deploymentWorker;

  public static void init(final Properties properties, final Map<Long, List<ProjectInfo>> projectUpdates,
      final Map<Long, List<ServiceInfo>> serviceUpdates) {
    synchronized (PullRequestDeploymentFactory.class) {
      if (deploymentWorker == null) {
        deploymentWorker = new PullRequestDeploymentWorker(projectUpdates, serviceUpdates);
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
