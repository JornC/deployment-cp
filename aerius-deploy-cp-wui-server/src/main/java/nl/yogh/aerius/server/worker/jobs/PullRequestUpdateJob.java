package nl.yogh.aerius.server.worker.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ProjectType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.domain.ServiceType;
import nl.yogh.aerius.server.util.ApplicationConfiguration;
import nl.yogh.aerius.server.util.CmdUtil;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.util.ProjectTypeDirectoryUtil;

public class PullRequestUpdateJob implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestUpdateJob.class);

  private final PullRequestInfo info;
  private final Object grip;

  private final ApplicationConfiguration cfg;

  public PullRequestUpdateJob(final ApplicationConfiguration cfg, final PullRequestInfo info, final Object grip) {
    this.cfg = cfg;
    this.info = info;
    this.grip = grip;

    info.busy(true);

    LOG.info("Update job created:  {} -> {}", info.idx(), info.hash());
  }

  @Override
  public void run() {
    LOG.info("Update job started:  {} -> {}", info.idx(), info.hash());

    final HashMap<ProjectType, ProjectInfo> projects = fetchProjectInformation();

    synchronized (grip) {
      info.lastUpdated(System.currentTimeMillis());
      info.busy(false);
      info.incomplete(false);

      info.projects(projects);
    }

    LOG.info("Update job complete: {} -> {}", info.idx(), info.hash());
  }

  private HashMap<ProjectType, ProjectInfo> fetchProjectInformation() {
    final String idx = info.idx();

    try {
      final HashMap<ProjectType, ProjectInfo> products = new HashMap<>();

      // Ensure you are on the correct branch.
      try {
        cmd("git checkout master");
      } catch (final ProcessExitException e) {
        // Eat.
      }
      try {
        cmd("git reset --hard origin/master");
      } catch (final ProcessExitException e) {
        // Eat.
      }
      try {
        cmd("git branch -D PR-%s", idx);
      } catch (final ProcessExitException e) {
        // Eat.
      }

      cmd("git fetch origin pull/%s/head:PR-%s", idx, idx);
      cmd("git checkout PR-%s", idx);

      for (final ProjectType type : ProjectType.values()) {
        final Set<String> dirs = ProjectTypeDirectoryUtil.getProjectDirectories(type);
        if (dirs == null) {
          continue;
        }

        final ProjectInfo info = ProjectInfo.create().type(type).hash(findShaSum(dirs)).status(ProjectStatus.UNBUILT);

        try {
          if (!cmd("docker ps --filter status=running --format {{.Names}} | grep %s%s%s", type.name().toLowerCase(), idx, info.hash().toLowerCase())
              .isEmpty()) {
            // info.status(ProjectStatus.DEPLOYED);
          } else if (!cmd("docker ps --filter status=exited --format {{.Names}} | grep %s%s%s", type.name().toLowerCase(), idx,
              info.hash().toLowerCase())
              .isEmpty()) {
            // info.status(ProjectStatus.SUSPENDED);
          } else {}
        } catch (final ProcessExitException e) {
          // eat
        }

        if (LOG.isDebugEnabled()) {
          LOG.trace("Calculating sum for ProductType {} > {}", info.hash().substring(0, 8), type.name());
        }

        info.services(findServiceHash(type));
        products.put(type, info);
      }

      return products;
    } catch (IOException | InterruptedException | ProcessExitException e) {
      throw new RuntimeException(e);
    }
  }

  private ArrayList<ServiceInfo> findServiceHash(final ProjectType type) throws IOException, InterruptedException, ProcessExitException {
    final ArrayList<ServiceInfo> lst = new ArrayList<>();

    for (final ServiceType serviceType : type.getServiceTypes()) {
      final String sha = findShaSum(ProjectTypeDirectoryUtil.getServiceDirectories(serviceType));

      final ServiceInfo service = ServiceInfo.create().hash(sha).type(serviceType).status(ServiceStatus.UNBUILT);

      try {
        if (!cmd("docker images -f \"label=nl.aerius.docker.service.type=%s\" -f label=nl.aerius.docker.service.hash=%s --format \"{{.Tag}}\"",
            serviceType.name(), service.hash()).isEmpty()) {
          service.status(ServiceStatus.BUILT);
        }
      } catch (final ProcessExitException e) {
        // eat
      }

      lst.add(service);

      if (LOG.isDebugEnabled()) {
        LOG.trace("Calculating sum for ServiceType {} > {}", sha.substring(0, 8), serviceType.name());
      }
    }

    return lst;
  }

  private String findShaSum(final Collection<String> dirs) throws IOException, InterruptedException, ProcessExitException {
    return findShaSum(String.join(" ", dirs));
  }

  private String findShaSum(final String[] dirs) throws IOException, InterruptedException, ProcessExitException {
    return findShaSum(String.join(" ", dirs));
  }

  private String findShaSum(final String dirs) throws IOException, InterruptedException, ProcessExitException {
    // return cmd("find %s -type f -exec sha256sum {} \\; | sha256sum", dirs).get(0);
    return cmd("find %s -type f -print0 | xargs -0 sha256sum | sha256sum | cut -d \" \" -f1", dirs).get(0);
  }

  private ArrayList<String> cmd(final String format, final String... args) throws IOException, InterruptedException, ProcessExitException {
    return cmd(String.format(format, (Object[]) args));
  }

  private ArrayList<String> cmd(final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmd(cfg.getMaintenanceBaseDir(), cmd);
  }
}
