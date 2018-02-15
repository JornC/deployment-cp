package nl.yogh.aerius.server.service;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.CompositionDeploymentAction;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.exception.ApplicationException;
import nl.yogh.aerius.builder.service.PullRequestService;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;
import nl.yogh.aerius.server.worker.ProjectUpdateRepositoryFactory;
import nl.yogh.aerius.server.worker.PullRequestDeploymentWorker;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceWorker;
import nl.yogh.aerius.server.worker.ServiceUpdateRepositoryFactory;

public class PullRequestServiceImpl implements PullRequestService {
  private final PullRequestMaintenanceWorker maintenanceInstance;
  private final PullRequestDeploymentWorker deploymentInstance;

  public PullRequestServiceImpl(final PullRequestMaintenanceWorker maintenanceInstance, final PullRequestDeploymentWorker deploymentInstance) {
    this.deploymentInstance = deploymentInstance;
    this.maintenanceInstance = maintenanceInstance;
  }

  @Override
  public ArrayList<PullRequestInfo> getPullRequests() throws ApplicationException {
    ArrayList<PullRequestInfo> pulls;
    synchronized (maintenanceInstance) {
      pulls = maintenanceInstance.getPullRequests();
    }
    return pulls;
  }

  @Override
  public CompositionInfo doAction(final String idx, final CompositionDeploymentAction action, final CompositionInfo info)
      throws ApplicationException {
    deploymentInstance.doAction(idx, action, info);

    return info;
  }

  @Override
  public ArrayList<CompositionInfo> getProductUpdates(final long since) throws ApplicationException {
    final TimestampedMultiMap<CompositionInfo> instance = ProjectUpdateRepositoryFactory.getInstance();

    ArrayList<CompositionInfo> updates;
    synchronized (instance) {
      updates = instance.getUpdates(since);
    }
    return updates;
  }

  @Override
  public ArrayList<ServiceInfo> getServiceUpdates(final long since) throws ApplicationException {
    final TimestampedMultiMap<ServiceInfo> instance = ServiceUpdateRepositoryFactory.getInstance();

    ArrayList<ServiceInfo> updates;
    synchronized (instance) {
      updates = instance.getUpdates(since);
    }
    return updates;
  }

  @Override
  public PresentSnapshot getPresentSituation() throws ApplicationException {
    final PresentSnapshot snapshot = new PresentSnapshot();
    synchronized (deploymentInstance) {
      snapshot.setProjects(deploymentInstance.getProjects());
      snapshot.setServices(deploymentInstance.getServices());
      snapshot.setLastUpdate(maintenanceInstance.getLastUpdate());
    }

    return snapshot;
  }
}
