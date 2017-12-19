package nl.yogh.aerius.server.worker.jobs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;

public abstract class ProjectJob implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(ProjectJob.class);

  protected final ProjectInfo info;

  protected final Map<Long, List<ProjectInfo>> projectUpdates;
  private final ConcurrentMap<String, ProjectInfo> projects;

  private final Map<Long, List<ServiceInfo>> serviceUpdates;
  protected final ConcurrentMap<String, ServiceInfo> services;

  public ProjectJob(final ProjectInfo info, final Map<Long, List<ProjectInfo>> projectUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates,
      final ConcurrentMap<String, ProjectInfo> projects, final ConcurrentMap<String, ServiceInfo> services) {
    this.info = info;
    this.projectUpdates = projectUpdates;
    this.serviceUpdates = serviceUpdates;
    this.projects = projects;
    this.services = services;
  }

  protected void putService(final ServiceInfo service) {
    synchronized (services) {
      services.put(service.hash(), service);
    }

    updateService(service);
  }

  protected void putProject(final ProjectInfo product) {
    synchronized (projects) {
      projects.put(product.hash(), product);
    }

    updateProject(product);
  }

  protected void updateProject(final ProjectInfo info) {
    synchronized (projectUpdates) {
      projectUpdates.get(System.currentTimeMillis()).add(info);
    }

    LOG.debug("Product updated. {} -> {}", info.hash(), info.status());
  }

  private void updateService(final ServiceInfo info) {
    synchronized (serviceUpdates) {
      serviceUpdates.get(System.currentTimeMillis()).add(info);
    }

    LOG.debug("Service updated. {} -> {}", info.hash(), info.status());
  }
}
