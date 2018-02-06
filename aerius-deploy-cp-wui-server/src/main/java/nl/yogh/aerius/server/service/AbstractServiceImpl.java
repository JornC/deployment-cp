package nl.yogh.aerius.server.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.exception.ApplicationException;
import nl.yogh.aerius.builder.exception.ApplicationException.Reason;
import nl.yogh.aerius.server.worker.PullRequestDeploymentFactory;
import nl.yogh.aerius.server.worker.PullRequestDeploymentWorker;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceFactory;
import nl.yogh.aerius.server.worker.PullRequestMaintenanceWorker;

public abstract class AbstractServiceImpl {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceImpl.class);

  protected PullRequestMaintenanceWorker getMaintenanceInstance() throws ApplicationException {
    PullRequestMaintenanceWorker instance;
    try {
      instance = PullRequestMaintenanceFactory.getInstance();
    } catch (final IOException e) {
      LOG.error("IOException while retrieving {}.", getClass().getSimpleName(), e);
      throw new ApplicationException(Reason.INTERNAL_ERROR);
    }

    return instance;
  }

  protected PullRequestDeploymentWorker getDeploymentInstance() throws ApplicationException {
    PullRequestDeploymentWorker instance;
    try {
      instance = PullRequestDeploymentFactory.getInstance();
    } catch (final IOException e) {
      LOG.error("IOException while retrieving {}.", getClass().getSimpleName(), e);
      throw new ApplicationException(Reason.INTERNAL_ERROR);
    }

    return instance;
  }
}
