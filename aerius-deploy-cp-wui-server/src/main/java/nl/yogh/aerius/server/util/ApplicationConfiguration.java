package nl.yogh.aerius.server.util;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Stream;

import nl.yogh.aerius.builder.domain.ProjectType;

public class ApplicationConfiguration {
  private final Properties properties;

  public ApplicationConfiguration(final Properties properties) {
    this.properties = properties;
  }

  public String getGithubOpenAuthToken() {
    return getPropertyRequired(properties, "cp.github.oath.token");
  }

  public String getMaintenanceBaseDir() {
    return getPropertyRequired(properties, "cp.maintenance.git.repo.dir");
  }

  public String getStagingDir() {
    return getPropertyRequired(properties, "cp.staging.dir");
  }

  public String getDbDataDir() {
    return getPropertyRequired(properties, "cp.data.dbdata");
  }

  private static String getPropertyRequired(final Properties properties, final String key) {
    final String prop = properties.getProperty(key);
    if (prop == null) {
      throw new IllegalStateException("Required property not present: " + key);
    }

    return prop;
  }

  public Stream<Entry<Object, Object>> getControlPanelProperties() {
    return properties.entrySet().stream().filter(e -> ((String) e.getKey()).startsWith("cp."));
  }

  public String getDeploymentHost(final ProjectType projectType) {
    return getPropertyRequired(properties, String.format("cp.deployment.%s.host", projectType.name()));
  }
}
