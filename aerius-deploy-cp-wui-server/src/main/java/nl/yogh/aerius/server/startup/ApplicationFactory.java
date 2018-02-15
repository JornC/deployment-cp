package nl.yogh.aerius.server.startup;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.worker.ProjectUpdateRepositoryFactory;
import nl.yogh.aerius.server.worker.PullRequestDeploymentFactory;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceFactory;
import nl.yogh.aerius.server.worker.ServiceUpdateRepositoryFactory;

/**
 * Should just set up injection but... oh well.
 */
public class ApplicationFactory {
  private static final Logger LOG = LoggerFactory.getLogger(ApplicationFactory.class);

  public static void init(final Properties properties) {
    final ConcurrentMap<String, CompositionInfo> projects = new ConcurrentHashMap<>();
    final ConcurrentMap<String, ServiceInfo> services = new ConcurrentHashMap<>();

    final TimestampedMultiMap<ServiceInfo> serviceUpdates = ServiceUpdateRepositoryFactory.getInstance();
    final TimestampedMultiMap<CompositionInfo> projectUpdates = ProjectUpdateRepositoryFactory.getInstance();

    final ApplicationConfiguration cfg = ConfigurationLoader.generate();

    PullRequestMaintenanceFactory.init(cfg, projects, services, projectUpdates);
    PullRequestDeploymentFactory.init(cfg, projects, services, projectUpdates, serviceUpdates);

    LOG.info("Application initialized.");
  }

  public static void shutdown() {
    PullRequestMaintenanceFactory.shutdown();
    PullRequestDeploymentFactory.shutdown();
  }
}
