package nl.yogh.aerius.server.registry;

import nl.yogh.aerius.builder.service.ServiceURLConstants;
import nl.yogh.aerius.server.service.DockerManagementServiceImpl;
import nl.yogh.aerius.server.service.LogServiceImpl;
import nl.yogh.aerius.server.service.PullRequestServiceImpl;

public class BuilderServiceRegistry extends AbstractServiceRegistry {
  public BuilderServiceRegistry() {
    addService(ServiceURLConstants.PULL_REQUEST_RELATIVE_PATH, new PullRequestServiceImpl());
    addService(ServiceURLConstants.DOCKER_RELATIVE_PATH, new DockerManagementServiceImpl());
    addService(ServiceURLConstants.LOG_RELATIVE_PATH, new LogServiceImpl());
  }
}
