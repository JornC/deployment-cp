package nl.yogh.aerius.server.worker.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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

  private final PullRequestInfo pullInfo;
  private final Object grip;

  private final ApplicationConfiguration cfg;

  private final ConcurrentMap<String, ProjectInfo> projects;
  private final ConcurrentMap<String, ServiceInfo> services;

  public PullRequestUpdateJob(final ApplicationConfiguration cfg, final PullRequestInfo info, final Object grip,
      final ConcurrentMap<String, ProjectInfo> projects, final ConcurrentMap<String, ServiceInfo> services) {
    this.cfg = cfg;
    this.pullInfo = info;
    this.grip = grip;
    this.projects = projects;
    this.services = services;

    info.busy(true);

    LOG.info("Update job created:  {} -> {}", info.idx(), info.hash());
  }

  @Override
  public void run() {
    LOG.info("Update job started:  {} -> {}", pullInfo.idx(), pullInfo.hash());

    final HashMap<ProjectType, ProjectInfo> projects = fetchProjectInformation();

    synchronized (grip) {
      pullInfo.lastUpdated(System.currentTimeMillis());
      pullInfo.busy(false);
      pullInfo.incomplete(false);

      pullInfo.projects(projects);
    }

    LOG.info("Update job complete: {} -> {}", pullInfo.idx(), pullInfo.hash());
  }

  private HashMap<ProjectType, ProjectInfo> fetchProjectInformation() {
    final String idx = pullInfo.idx();

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

        final ProjectInfo projectInfo = ProjectInfo.create()
            .type(type)
            .hash(findShaSum(dirs))
            .buildHash(pullInfo.hash());

        ProjectStatus status = ProjectStatus.UNBUILT;

        try {
          if (!cmd("docker ps --filter status=running --format {{.Names}} | grep %s%s%s", type.name().toLowerCase(), idx,
              projectInfo.hash().toLowerCase())
                  .isEmpty()) {
            status = ProjectStatus.DEPLOYED;
          } else if (!cmd("docker ps --filter status=exited --format {{.Names}} | grep %s%s%s", type.name().toLowerCase(), idx,
              projectInfo.hash().toLowerCase())
                  .isEmpty()) {
            status = ProjectStatus.SUSPENDED;
          } else {}
        } catch (final ProcessExitException e) {
          // eat
        }

        projectInfo.status(status);

        if (projects.containsKey(projectInfo.hash())) {
          projects.get(projectInfo.hash()).status(status);
        } else if (status == ProjectStatus.DEPLOYED) {
          projectInfo.url(String.format(cfg.getDeploymentHost(projectInfo.type()), idx));
          projects.put(projectInfo.hash(), projectInfo);
        }

        if (LOG.isDebugEnabled()) {
          LOG.trace("Calculating sum for ProductType {} > {}", projectInfo.hash().substring(0, 8), type.name());
        }

        projectInfo.services(findServiceHash(type));
        products.put(type, projectInfo);
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

      final ServiceInfo serviceInfo = ServiceInfo.create().hash(sha).type(serviceType).status(ServiceStatus.UNBUILT);

      try {
        if (!cmd("docker images -f \"label=nl.aerius.docker.service.type=%s\" -f label=nl.aerius.docker.service.hash=%s --format \"{{.Tag}}\"",
            serviceType.name(), serviceInfo.hash()).isEmpty()) {
          serviceInfo.status(ServiceStatus.BUILT);

          if (services.containsKey(serviceInfo.hash())) {
            services.get(serviceInfo.hash()).status(ServiceStatus.BUILT);
          }
        }
      } catch (final ProcessExitException e) {
        // eat
      }

      lst.add(serviceInfo);

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
