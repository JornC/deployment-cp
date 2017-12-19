package nl.yogh.aerius.server.worker.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.domain.ServiceType;
import nl.yogh.aerius.server.util.CmdUtil;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.ProductTypeDirectoryUtil;

public class PullRequestUpdateJob implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestUpdateJob.class);

  private final PullRequestInfo info;
  private final Object grip;

  private final String baseDir;

  public PullRequestUpdateJob(final String baseDir, final PullRequestInfo info, final Object grip) {
    this.baseDir = baseDir;
    this.info = info;
    this.grip = grip;

    info.busy(true);

    LOG.info("Update job created:  {} -> {}", info.idx(), info.hash());
  }

  @Override
  public void run() {
    LOG.info("Update job started:  {} -> {}", info.idx(), info.hash());

    final HashMap<ProductType, ProductInfo> products = fetchProductInformation();

    synchronized (grip) {
      info.lastUpdated(System.currentTimeMillis());
      info.busy(false);
      info.incomplete(false);

      info.products(products);
    }

    LOG.info("Update job complete: {} -> {}", info.idx(), info.hash());
  }

  private HashMap<ProductType, ProductInfo> fetchProductInformation() {
    final String idx = info.idx();

    try {
      final HashMap<ProductType, ProductInfo> products = new HashMap<>();

      // Ensure you are on the correct branch.
      cmd("git fetch origin pull/%s/head:PR-%s", idx, idx);
      cmd("git checkout PR-%s", idx);

      for (final ProductType type : ProductType.values()) {
        final String dirs = ProductTypeDirectoryUtil.getProductDirectories(type);
        if (dirs == null) {
          continue;
        }

        final ProductInfo info = ProductInfo.create().hash(findShaSum(dirs)).status(ServiceStatus.UNBUILT);

        if (LOG.isDebugEnabled()) {
          LOG.debug("Calculating sum for ProductType {} > {}", info.hash().substring(0, 8), type.name());
        }

        info.services(findServiceHash(type));
        products.put(type, info);

      }

      return products;
    } catch (IOException | InterruptedException | ProcessExitException e) {
      throw new RuntimeException(e);
    }
  }

  private ArrayList<String> findServiceHash(final ProductType type) throws IOException, InterruptedException, ProcessExitException {
    final ArrayList<String> lst = new ArrayList<>();

    for (final ServiceType serviceType : type.getServiceTypes()) {
      final String sha = findShaSum(ProductTypeDirectoryUtil.getServiceDirectories(serviceType));

      lst.add(sha);

      if (LOG.isDebugEnabled()) {
        LOG.debug("Calculating sum for ServiceType {} > {}", sha.substring(0, 8), serviceType.name());
      }
    }

    return lst;
  }

  // ./aerius-database-common ./aerius-database-calculator
  private String findShaSum(final String dirs) throws IOException, InterruptedException, ProcessExitException {
    return cmd("find %s -type f -print0 | xargs -0 sha256sum | sha256sum | cut -d \" \" -f1", dirs).get(0);
  }

  private ArrayList<String> cmd(final String format, final String... args) throws IOException, InterruptedException, ProcessExitException {
    return cmd(String.format(format, (Object[]) args));
  }

  private ArrayList<String> cmd(final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmd(baseDir, cmd);
  }
}
