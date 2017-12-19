package nl.yogh.aerius.server.service;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.exception.ApplicationException;
import nl.yogh.aerius.builder.exception.ApplicationException.Reason;
import nl.yogh.aerius.builder.service.ProductDeploymentAction;
import nl.yogh.aerius.builder.service.PullRequestService;
import nl.yogh.aerius.server.worker.PullRequestDeploymentFactory;
import nl.yogh.aerius.server.worker.PullRequestDeploymentWorker;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceFactory;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceWorker;

public class PullRequestServiceImpl implements PullRequestService {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestMaintenanceWorker.class);

  @Override
  public ArrayList<PullRequestInfo> getPullRequests() throws ApplicationException {
    final PullRequestMaintenanceWorker instance = getPullInstance();

    ArrayList<PullRequestInfo> pulls;
    synchronized (instance) {
      pulls = instance.getPullRequests();
    }
    return pulls;
  }

  @Override
  public ProductInfo doAction(final ProductType type, final ProductDeploymentAction action, final ProductInfo info) throws ApplicationException {
    final PullRequestDeploymentWorker instance = getDeploymentInstance();
    instance.doAction(type, action, info);

    LOG.info("Doing [{}] -- current status: {}", action, info.status());

    info.busy(true);
    return info;
  }

  @Override
  public ArrayList<ProductInfo> getProductUpdates(final long since) throws ApplicationException {
    final PullRequestDeploymentWorker instance = getDeploymentInstance();

    ArrayList<ProductInfo> updates;
    synchronized (instance) {
      updates = instance.getProductUpdates(since);
    }
    return updates;
  }

  @Override
  public PresentSnapshot getPresentSituation() throws ApplicationException {
    final PullRequestDeploymentWorker instance = getDeploymentInstance();

    final ArrayList<ServiceInfo> services;
    final ArrayList<ProductInfo> products;
    final PresentSnapshot snapshot = new PresentSnapshot();
    synchronized (instance) {
      snapshot.setProducts(instance.getProducts());
      snapshot.setServices(instance.getServices());
    }

    return snapshot;
  }

  @Override
  public ArrayList<ServiceInfo> getServiceUpdates(final long since) throws ApplicationException {
    final PullRequestDeploymentWorker instance = getDeploymentInstance();

    ArrayList<ServiceInfo> updates;
    synchronized (instance) {
      updates = instance.getServiceUpdates(since);
    }
    return updates;
  }

  private PullRequestMaintenanceWorker getPullInstance() throws ApplicationException {
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
