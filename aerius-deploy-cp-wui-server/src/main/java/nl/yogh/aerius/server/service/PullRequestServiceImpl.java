package nl.yogh.aerius.server.service;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.domain.ProjectDeploymentAction;
import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.exception.ApplicationException;
import nl.yogh.aerius.builder.exception.ApplicationException.Reason;
import nl.yogh.aerius.builder.service.PullRequestService;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;
import nl.yogh.aerius.server.worker.ProjectUpdateRepositoryFactory;
import nl.yogh.aerius.server.worker.PullRequestDeploymentFactory;
import nl.yogh.aerius.server.worker.PullRequestDeploymentWorker;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceFactory;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceWorker;
import nl.yogh.aerius.server.worker.ServiceUpdateRepositoryFactory;

public class PullRequestServiceImpl implements PullRequestService {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestServiceImpl.class);

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
  public ProjectInfo doAction(final String idx, final ProjectType type, final ProjectDeploymentAction action, final ProjectInfo info)
      throws ApplicationException {
    final PullRequestDeploymentWorker instance = getDeploymentInstance();
    instance.doAction(idx, type, action, info);

    LOG.info("Doing action [{}] on {} -- current status: {}", action, info.hash(), info.status());

    return info;
  }

  @Override
  public ArrayList<ProjectInfo> getProductUpdates(final long since) throws ApplicationException {
    final TimestampedMultiMap<ProjectInfo> instance = ProjectUpdateRepositoryFactory.getInstance();

    ArrayList<ProjectInfo> updates;
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
      snapshot.setProducts(instance.getProjects());
      snapshot.setServices(instance.getServices());
      snapshot.setLastUpdate(maintainer.getLastUpdate());
    }

    return snapshot;
  }

  private PullRequestMaintenanceWorker getMaintenanceInstance() throws ApplicationException {
    PullRequestMaintenanceWorker instance;
    try {
      instance = PullRequestMaintenanceFactory.getInstance();
    } catch (final IOException e) {
      LOG.error("IOException while retrieving pull request maintenance worker.", e);
      throw new ApplicationException(Reason.INTERNAL_ERROR);
    }

    return instance;
  }

  private PullRequestDeploymentWorker getDeploymentInstance() throws ApplicationException {
    PullRequestDeploymentWorker instance;
    try {
      instance = PullRequestDeploymentFactory.getInstance();
    } catch (final IOException e) {
      LOG.error("IOException while retrieving pull request deployment worker.", e);
      throw new ApplicationException(Reason.INTERNAL_ERROR);
    }

    return instance;
  }
}
