package nl.yogh.aerius.server.worker;

import java.util.HashMap;

import org.essentials4j.New;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;

public class PullRequestUpdateJob implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestUpdateJob.class);

  private final PullRequestInfo info;
  private final Object grip;

  public PullRequestUpdateJob(final PullRequestInfo info, final Object grip) {
    this.info = info;
    this.grip = grip;

    info.busy(true);

    LOG.info("Update job created:  {} -> {}", info.idx(), info.hash());
  }

  @Override
  public void run() {
    LOG.info("Update job started:  {} -> {}", info.idx(), info.hash());

    try {
      Thread.sleep(5000);
    } catch (final InterruptedException e) {
      LOG.error("Interrupt!", e);
    }

    synchronized (grip) {
      info.lastUpdated(System.currentTimeMillis());
      info.busy(false);
      info.incomplete(false);
      info.products(new HashMap<>(New.map(
          ProductType.CALCULATOR, ProductInfo.create().hash("d8e1db").status(ServiceStatus.RUNNING),
          ProductType.SCENARIO, ProductInfo.create().hash("3b6a52").status(ServiceStatus.SUSPENDED),
          ProductType.WORKERS, ProductInfo.create().hash("aa2e1f").status(ServiceStatus.UNBUILT),
          ProductType.TESTS, ProductInfo.create().hash("e1dbd8").status(ServiceStatus.UNBUILT))));
    }

    LOG.info("Update job complete: {} -> {}", info.idx(), info.hash());
  }
}
