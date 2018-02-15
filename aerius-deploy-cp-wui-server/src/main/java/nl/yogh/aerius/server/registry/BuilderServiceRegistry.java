package nl.yogh.aerius.server.registry;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.service.ServiceURLConstants;
import nl.yogh.aerius.server.service.ApplicationServiceImpl;
import nl.yogh.aerius.server.service.DockerManagementServiceImpl;
import nl.yogh.aerius.server.service.LogServiceImpl;
import nl.yogh.aerius.server.service.PullRequestServiceImpl;
import nl.yogh.aerius.server.startup.ConfigurationLoader;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.worker.PullRequestDeploymentFactory;
import nl.yogh.aerius.server.worker.PullRequestDeploymentWorker;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceFactory;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceWorker;

public class BuilderServiceRegistry extends AbstractServiceRegistry {
  private static final Logger LOG = LoggerFactory.getLogger(DockerManagementServiceImpl.class);

  public BuilderServiceRegistry() {
    final ApplicationConfiguration cfg = getApplicationConfig();
    final PullRequestMaintenanceWorker maintenanceInstance = getMaintenanceInstance();
    final PullRequestDeploymentWorker deploymentInstance = getDeploymentInstance();

    addService(ServiceURLConstants.PULL_REQUEST_RELATIVE_PATH, new PullRequestServiceImpl(maintenanceInstance, deploymentInstance));
    addService(ServiceURLConstants.DOCKER_RELATIVE_PATH, new DockerManagementServiceImpl(maintenanceInstance, deploymentInstance));
    addService(ServiceURLConstants.LOG_RELATIVE_PATH, new LogServiceImpl());
    addService(ServiceURLConstants.APPLICATION_REQUEST_RELATIVE_PATH, new ApplicationServiceImpl(cfg));
  }

  private ApplicationConfiguration getApplicationConfig() {
    return ConfigurationLoader.getCachedInstance();
  }

  protected PullRequestMaintenanceWorker getMaintenanceInstance() {
    PullRequestMaintenanceWorker instance;
    try {
      instance = PullRequestMaintenanceFactory.getInstance();
    } catch (final IOException e) {
      LOG.error("IOException while retrieving {}.", getClass().getSimpleName(), e);
      throw new RuntimeException("Could not initialize service registry due to error while getting maintenance instance", e);
    }

    return instance;
  }

  protected PullRequestDeploymentWorker getDeploymentInstance() {
    PullRequestDeploymentWorker instance;
    try {
      instance = PullRequestDeploymentFactory.getInstance();
    } catch (final IOException e) {
      LOG.error("IOException while retrieving {}.", getClass().getSimpleName(), e);
      throw new RuntimeException("Could not initialize service registry due to error while getting deployment instance", e);
    }

    return instance;
  }
}
