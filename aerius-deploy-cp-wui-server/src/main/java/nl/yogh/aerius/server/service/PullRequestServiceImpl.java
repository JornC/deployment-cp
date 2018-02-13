package nl.yogh.aerius.server.service;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.domain.CompositionDeploymentAction;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.exception.ApplicationException;
import nl.yogh.aerius.builder.service.PullRequestService;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;
import nl.yogh.aerius.server.worker.ProjectUpdateRepositoryFactory;
import nl.yogh.aerius.server.worker.PullRequestDeploymentWorker;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceWorker;
import nl.yogh.aerius.server.worker.ServiceUpdateRepositoryFactory;

public class PullRequestServiceImpl extends AbstractServiceImpl implements PullRequestService {
  @Override
  public ArrayList<PullRequestInfo> getPullRequests() throws ApplicationException {
    final PullRequestMaintenanceWorker instance = getMaintenanceInstance();

    ArrayList<PullRequestInfo> pulls;
    synchronized (instance) {
      pulls = instance.getPullRequests();
    }
    return pulls;
  }

  @Override
  public CompositionInfo doAction(final String idx, final CompositionDeploymentAction action, final CompositionInfo info) throws ApplicationException {
    final PullRequestDeploymentWorker instance = getDeploymentInstance();
    instance.doAction(idx, action, info);

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
    final PullRequestDeploymentWorker instance = getDeploymentInstance();
    final PullRequestMaintenanceWorker maintainer = getMaintenanceInstance();

    final PresentSnapshot snapshot = new PresentSnapshot();
    synchronized (instance) {
      snapshot.setProjects(instance.getProjects());
      snapshot.setServices(instance.getServices());
      snapshot.setLastUpdate(maintainer.getLastUpdate());
    }

    return snapshot;
  }
}
