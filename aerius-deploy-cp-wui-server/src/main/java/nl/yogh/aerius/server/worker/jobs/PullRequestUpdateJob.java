package nl.yogh.aerius.server.worker.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.CommitInfo;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.CompositionStatus;
import nl.yogh.aerius.builder.domain.CompositionType;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.domain.ServiceType;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.HashUtil;

public class PullRequestUpdateJob implements Runnable {
  private static final String MATCHALL = "matchall";

  private static final Logger LOG = LoggerFactory.getLogger(PullRequestUpdateJob.class);

  private final CommitInfo pullInfo;
  private final Object grip;

  private final ApplicationConfiguration cfg;

  private final ConcurrentMap<String, CompositionInfo> compositions;
  private final ConcurrentMap<String, ServiceInfo> services;

  public PullRequestUpdateJob(final ApplicationConfiguration cfg, final CommitInfo info, final Object grip,
      final ConcurrentMap<String, CompositionInfo> compositions, final ConcurrentMap<String, ServiceInfo> services) {
    this.cfg = cfg;
    this.pullInfo = info;
    this.grip = grip;
    this.compositions = compositions;
    this.services = services;

    info.busy(true);

    LOG.info("Update job created:  {} -> {}", info.idx(), info.hash());
  }

  @Override
  public void run() {
    LOG.info("Update job started:  {} -> {}", pullInfo.idx(), pullInfo.hash());

    final HashMap<CompositionType, CompositionInfo> compositions = fetchCompositionInformation();

    synchronized (grip) {
      pullInfo.lastUpdated(System.currentTimeMillis());
      pullInfo.busy(false);
      pullInfo.incomplete(false);

      pullInfo.compositions(compositions);
    }

    LOG.info("Update job complete: {} -> {}", pullInfo.idx(), pullInfo.hash());
  }

  private HashMap<CompositionType, CompositionInfo> fetchCompositionInformation() {
    final String idx = pullInfo.idx();

    try {
      final HashMap<CompositionType, CompositionInfo> products = new HashMap<>();

      // Ensure you are on the correct branch.
      try {
        cmd("git checkout master");
        cmd("git fetch");
      } catch (final ProcessExitException e) {
        // Eat.
      }
      try {
        cmd("git reset --hard origin/master");
      } catch (final ProcessExitException e) {
        // Eat.
      }
      try {
        cmd("git branch -D commit-%s", idx);
      } catch (final ProcessExitException e) {
        // Eat.
      }

      if (pullInfo.isPull()) {
        cmd("git fetch origin pull/%s/head:commit-%s", idx, idx);
        cmd("git checkout commit-%s", idx);
      } else {
        cmd("git checkout -b commit-%s", idx);
        cmd("git reset --hard %s", pullInfo.hash());
      }

      for (final CompositionType type : cfg.getCompositionTypes()) {
        final Set<String> dirs = cfg.getProjectDirectories(type);
        if (dirs == null) {
          continue;
        }

        final CompositionInfo projectInfo = CompositionInfo.create()
            .type(type)
            .buildHash(pullInfo.hash())
            .commit(pullInfo);

        try {
          projectInfo.hash(findShaSum(dirs));
          projectInfo.services(findServiceHash(type));
          if (LOG.isDebugEnabled()) {
            LOG.trace("Calculating sum for ProductType {} > {}", HashUtil.shorten(projectInfo.hash()), type.name());
          }

          CompositionStatus status = CompositionStatus.UNBUILT;

          try {
            if (!cmd("docker ps --filter status=running --format {{.Names}} | grep %s%s%s", type.name().toLowerCase(), idx,
                projectInfo.hash().toLowerCase())
                    .isEmpty()) {
              status = CompositionStatus.DEPLOYED;
            } else if (!cmd("docker ps --filter status=exited --format {{.Names}} | grep %s%s%s", type.name().toLowerCase(), idx,
                projectInfo.hash().toLowerCase())
                    .isEmpty()) {
              status = CompositionStatus.SUSPENDED;
            } else {}
          } catch (final ProcessExitException e) {
            // eat
          }

          projectInfo.status(status);

          if (compositions.containsKey(projectInfo.hash())) {
            compositions.get(projectInfo.hash()).status(status);
          } else if (status == CompositionStatus.DEPLOYED) {
            projectInfo.url(String.format(cfg.getDeploymentHost(projectInfo.type()), idx));
            compositions.put(projectInfo.hash(), projectInfo);
          }

        } catch (final IllegalStateException e) {
          projectInfo.status(CompositionStatus.CORRUPTED);
          projectInfo.hash("N/A");
        } finally {
          products.put(type, projectInfo);
        }
      }

      return products;
    } catch (IOException | InterruptedException | ProcessExitException e) {
      throw new RuntimeException(e);
    }
  }

  private ArrayList<ServiceInfo> findServiceHash(final CompositionType type)
      throws IOException, InterruptedException, ProcessExitException, IllegalStateException {
    final ArrayList<ServiceInfo> lst = new ArrayList<>();

    for (final ServiceType serviceType : type.serviceTypes()) {
      final String sha = findShaSum(cfg.getServiceDirectories(serviceType));

      final ServiceInfo serviceInfo = ServiceInfo.create().hash(sha).type(serviceType).status(ServiceStatus.UNBUILT);

      try {
        if (!cmd("docker images -f \"label=nl.aerius.docker.service.type=%s\" -f label=nl.aerius.docker.service.hash=%s --format \"{{.Tag}}\"",
            serviceType.name(), serviceInfo.hash()).isEmpty()) {
          serviceInfo.status(ServiceStatus.BUILT);

          if (!services.containsKey(serviceInfo.hash())) {
            services.put(serviceInfo.hash(), serviceInfo);
          }
        }
      } catch (final ProcessExitException e) {
        // eat
      }

      lst.add(serviceInfo);

      if (LOG.isDebugEnabled()) {
        LOG.trace("Calculating sum for ServiceType {} > {}", HashUtil.shorten(sha), serviceType.name());
      }
    }

    return lst;
  }

  private String findShaSum(final Collection<String> dirs) throws IOException, InterruptedException, ProcessExitException, IllegalStateException {
    return findShaSum(String.join(" ", dirs));
  }

  private String findShaSum(final String dirs) throws IOException, InterruptedException, ProcessExitException, IllegalStateException {
    // return cmd("find %s -type f -exec sha256sum {} \\; | sha256sum", dirs).get(0);
    if (dirs.isEmpty()) {
      return MATCHALL;
    }

    final String ret = cmd("find %s -type f -print0 | xargs -0 sha256sum | sha256sum | cut -d \" \" -f1", dirs).get(0);
    if (ret.contains("No such file or directory")) {
      throw new IllegalStateException("Directory structure unexpected; build configuration incompatible with repository.");
    }
    return ret;
  }

  private ArrayList<String> cmd(final String format, final String... args) throws IOException, InterruptedException, ProcessExitException {
    return cmd(String.format(format, (Object[]) args));
  }

  private ArrayList<String> cmd(final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmd(cfg.getMaintenanceBaseDir(), cmd);
  }
}
