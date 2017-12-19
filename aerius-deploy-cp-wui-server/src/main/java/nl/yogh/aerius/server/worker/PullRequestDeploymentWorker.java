package nl.yogh.aerius.server.worker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectType;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.service.ProjectDeploymentAction;
import nl.yogh.aerius.server.worker.jobs.CatchAllRunnable;
import nl.yogh.aerius.server.worker.jobs.ProjectCompilationJob;
import nl.yogh.aerius.server.worker.jobs.ProjectDeploymentJob;
import nl.yogh.aerius.server.worker.jobs.ProjectSuspensionJob;

public class PullRequestDeploymentWorker {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestDeploymentWorker.class);

  private static final int CACHE_MINUTES = 15;
  private static final long CACHE_MILLISECONDS = CACHE_MINUTES * 60 * 1000;

  private final ExecutorService projectCompilationExecutor;
  private final ExecutorService projectDeploymentExecutor;
  private final ExecutorService projectSuspensionExecutor;

  private final ScheduledExecutorService clearCacheExecutor;

  private final Map<Long, List<ProjectInfo>> projectUpdates;
  private final Map<Long, List<ServiceInfo>> serviceUpdates;

  private final ConcurrentMap<String, ProjectInfo> projects = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, ServiceInfo> services = new ConcurrentHashMap<>();

  public PullRequestDeploymentWorker(final Map<Long, List<ProjectInfo>> projectUpdates, final Map<Long, List<ServiceInfo>> serviceUpdates) {
    this.projectUpdates = projectUpdates;
    this.serviceUpdates = serviceUpdates;
    clearCacheExecutor = Executors.newSingleThreadScheduledExecutor();
    clearCacheExecutor.scheduleWithFixedDelay(() -> {
      final long clearBefore = new Date().getTime() - CACHE_MILLISECONDS;
      synchronized (projectUpdates) {
        projectUpdates.keySet().removeIf(o -> o < clearBefore);
      }

      synchronized (serviceUpdates) {
        serviceUpdates.keySet().removeIf(o -> o < clearBefore);
      }

      LOG.info("UpdateCache cleared up to {}", new Date(clearBefore).toString());
    }, 0, CACHE_MINUTES, TimeUnit.MINUTES);

    projectCompilationExecutor = Executors.newSingleThreadExecutor();
    projectDeploymentExecutor = Executors.newFixedThreadPool(2);
    projectSuspensionExecutor = Executors.newFixedThreadPool(2);
  }

  public void shutdown() {
    clearCacheExecutor.shutdownNow();
  }

  public void doAction(final String idx, final ProjectType type, final ProjectDeploymentAction action, final ProjectInfo info) {
    switch (action) {
    case BUILD:
      projectCompilationExecutor
          .submit(CatchAllRunnable.wrap(new ProjectCompilationJob(idx, type, info, projectUpdates, serviceUpdates, projects, services)));
      break;
    case SUSPEND:
      projectSuspensionExecutor.submit(CatchAllRunnable.wrap(new ProjectSuspensionJob(info, projectUpdates, serviceUpdates, projects, services)));
      break;
    case DEPLOY:
      projectDeploymentExecutor.submit(CatchAllRunnable.wrap(new ProjectDeploymentJob(info, projectUpdates, serviceUpdates, projects, services)));
      break;
    case DESTROY:
    default:
      LOG.info("Not implemented yet or unknown: {}", action);
    }
  }

  public ArrayList<ProjectInfo> getProjects() {
    return new ArrayList<>(projects.values());
  }

  public ArrayList<ServiceInfo> getServices() {
    return new ArrayList<>(services.values());
  }
}
