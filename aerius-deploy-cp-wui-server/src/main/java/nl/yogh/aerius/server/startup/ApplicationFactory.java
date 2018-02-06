package nl.yogh.aerius.server.startup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
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

  private static final String CONFIG_PROPERTIES_NAME = "deploycp.properties";

  public static void init(final Properties properties) {
    final ConcurrentMap<String, ProjectInfo> projects = new ConcurrentHashMap<>();
    final ConcurrentMap<String, ServiceInfo> services = new ConcurrentHashMap<>();

    final TimestampedMultiMap<ServiceInfo> serviceUpdates = ServiceUpdateRepositoryFactory.getInstance();
    final TimestampedMultiMap<ProjectInfo> projectUpdates = ProjectUpdateRepositoryFactory.getInstance();

    final Properties props = new Properties(System.getProperties());
    try {
      final int propNum = props.size();

      final InputStream stream = ApplicationFactory.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_NAME);
      props.load(stream);

      LOG.info("Loaded configuration ({} variables) from file: {}", props.size() - propNum,
          ApplicationFactory.class.getClassLoader().getResource(CONFIG_PROPERTIES_NAME).getPath().toString());
    } catch (final IOException e) {
      LOG.error("Could not load properties file from classpath: {} ({})", CONFIG_PROPERTIES_NAME, System.getProperty("java.class.path"), e);
    }

    final ApplicationConfiguration cfg = new ApplicationConfiguration(props);

    PullRequestMaintenanceFactory.init(cfg, projects, services, projectUpdates);
    PullRequestDeploymentFactory.init(cfg, projects, services, projectUpdates, serviceUpdates);
  }

  public static void shutdown() {
    PullRequestMaintenanceFactory.shutdown();
    PullRequestDeploymentFactory.shutdown();
  }
}
