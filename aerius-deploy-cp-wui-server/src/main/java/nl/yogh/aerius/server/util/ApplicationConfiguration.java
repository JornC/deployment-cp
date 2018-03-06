package nl.yogh.aerius.server.util;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.yogh.aerius.builder.domain.CompositionType;
import nl.yogh.aerius.builder.domain.ServiceType;
import nl.yogh.aerius.server.collections.MultiSet;

public class ApplicationConfiguration {
  private final Properties properties;
  private Set<CompositionType> compositionTypes;
  private MultiSet<ServiceType, String> serviceTypes;

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

  public String getGithubUserName() {
    return getPropertyRequired(properties, "cp.github.user");
  }

  public String getGithubRepositoryName() {
    return getPropertyRequired(properties, "cp.github.repository");
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

  public String getDeploymentHost(final CompositionType projectType) {
    return getPropertyRequired(properties, String.format("cp.deployment.%s.host", projectType.name()));
  }

  public Set<CompositionType> getCompositionTypes() {
    return compositionTypes;
  }

  public Set<String> getProjectDirectories(final CompositionType type) {
    return type.serviceTypes().stream()
        .map(v -> this.getServiceDirectories(v))
        .flatMap(v -> v.stream())
        .collect(Collectors.toSet());
  }

  public Set<String> getServiceDirectories(final ServiceType serviceType) {
    if (!serviceTypes.containsKey(serviceType)) {
      throw new RuntimeException("Unknown service requested; " + serviceType.name() + " staging hierarchy corrupted.");
    }

    return serviceTypes.get(serviceType);
  }

  public void setCompositionTypes(final Set<CompositionType> compositionTypes) {
    this.compositionTypes = compositionTypes;
  }

  public void setServiceTypes(final MultiSet<ServiceType, String> serviceTypes) {
    this.serviceTypes = serviceTypes;
  }
}
