package nl.yogh.aerius.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.DockerContainer;
import nl.yogh.aerius.builder.domain.DockerImage;
import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectStatus;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.exception.ApplicationException;
import nl.yogh.aerius.builder.service.DockerManagementService;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;
import nl.yogh.aerius.server.util.CmdUtil;
import nl.yogh.aerius.server.util.CmdUtil.ProcessExitException;
import nl.yogh.aerius.server.worker.ProjectUpdateRepositoryFactory;
import nl.yogh.aerius.server.worker.ServiceUpdateRepositoryFactory;

public class DockerManagementServiceImpl extends AbstractServiceImpl implements DockerManagementService {
  private static final Logger LOG = LoggerFactory.getLogger(PullRequestServiceImpl.class);

  private static final ExecutorService executor = Executors.newSingleThreadExecutor();

  @Override
  public void stopAllContainers() throws ApplicationException {
    executor.submit(() -> {
      LOG.info("[COMMAND] Stopping all containers.");

      try {
        cmd("docker stop $(docker ps -aq --filter status=running --filter label=nl.aerius.docker.service=true)");
        final TimestampedMultiMap<ProjectInfo> projectUpdates = ProjectUpdateRepositoryFactory.getInstance();
        getDeploymentInstance().getProjects().stream().forEach(v -> projectUpdates.timestamp(v.status(ProjectStatus.SUSPENDED)));
      } catch (IOException | InterruptedException | ProcessExitException | ApplicationException e) {
        LOG.error("Error during stopAll()", e);
      } finally {
        LOG.info("Done stopping all containers.");
      }
    });
  }

  @Override
  public void removeAllContainers() throws ApplicationException {
    executor.submit(() -> {
      LOG.info("[COMMAND] Removing all containers.");

      try {
        cmd("docker rm $(docker ps -aq --filter status=exited --filter label=nl.aerius.docker.service=true)");
        final TimestampedMultiMap<ProjectInfo> projectUpdates = ProjectUpdateRepositoryFactory.getInstance();
        getDeploymentInstance().getProjects().stream().forEach(v -> projectUpdates.timestamp(v.status(ProjectStatus.UNBUILT)));
      } catch (IOException | InterruptedException | ProcessExitException | ApplicationException e) {
        LOG.error("Error during stopAll()", e);
      } finally {
        LOG.info("[COMMAND] Done removing all containers.");
      }
    });
  }

  @Override
  public void removeAllImages() throws ApplicationException {
    executor.submit(() -> {
      LOG.info("[COMMAND] Pruning images.");

      try {
        cmd("docker rmi $(docker images -q --filter label=nl.aerius.docker.service=true)");
        final TimestampedMultiMap<ServiceInfo> serviceUpdates = ServiceUpdateRepositoryFactory.getInstance();
        getDeploymentInstance().getServices().stream().forEach(v -> serviceUpdates.timestamp(v.status(ServiceStatus.UNBUILT)));
      } catch (IOException | InterruptedException | ProcessExitException | ApplicationException e) {
        LOG.error("Error during stopAll()", e);
      } finally {
        LOG.info("[COMMAND] Done pruning images.");
      }
    });
  }

  @Override
  public ArrayList<DockerContainer> retrieveContainers() throws ApplicationException {
    final ArrayList<DockerContainer> containers = new ArrayList<>();

    try {
      cmd("docker ps -a --format {{.ID}},{{.Image}},{{.Names}} --filter label=nl.aerius.docker.service=true").stream()
          .map(v -> v.split(","))
          .map(v -> DockerContainer.create().hash(v[0]).image(v[1]).name(v[2]))
          .forEach(v -> containers.add(v));
    } catch (IOException | InterruptedException | ProcessExitException e) {
      LOG.error("Error during stopAll()", e);
    }

    LOG.info("Returning {} containers", containers.size());

    return containers;
  }

  @Override
  public ArrayList<DockerImage> retrieveImages() throws ApplicationException {
    final ArrayList<DockerImage> images = new ArrayList<>();

    try {
      cmd("docker images --format {{.ID}},{{.Repository}},{{.Tag}} --filter label=nl.aerius.docker.service=true").stream()
          .map(v -> v.split(","))
          .map(v -> DockerImage.create().hash(v[0]).name(v[1]).tag(v[2]))
          .forEach(v -> images.add(v));
    } catch (IOException | InterruptedException | ProcessExitException e) {
      LOG.error("Error during stopAll()", e);
    }

    LOG.info("Returning {} images", images.size());

    return images;
  }

  @Override
  public boolean stopContainer(final DockerContainer container) {
    final CountDownLatch latch = new CountDownLatch(1);

    executor.submit(() -> {
      LOG.info("[COMMAND] Stopping container {}", container.hash());

      try {
        cmd("docker stop %s", container.hash());
        latch.countDown();
      } catch (IOException | InterruptedException | ProcessExitException e) {
        LOG.error("Error during stopContainer {}", container.hash(), e);
      } finally {
        LOG.info("[COMMAND] Done stopping container {}", container.hash());
      }
    });

    return await(latch);
  }

  @Override
  public boolean removeContainer(final DockerContainer container) {
    final CountDownLatch latch = new CountDownLatch(1);

    executor.submit(() -> {
      LOG.info("[COMMAND] Removing container {}", container.hash());

      try {
        cmd("docker rm %s", container.hash());
        latch.countDown();
      } catch (IOException | InterruptedException | ProcessExitException e) {
        LOG.error("Error during removeContainer {}", container.hash(), e);
      } finally {
        LOG.info("[COMMAND] Done removing container {}", container.hash());
      }
    });

    return await(latch);
  }

  private boolean await(final CountDownLatch latch) {
    try {
      return latch.await(5, TimeUnit.SECONDS);
    } catch (final InterruptedException e) {}

    return false;
  }

  @Override
  public boolean removeImage(final DockerImage image) throws ApplicationException {
    final CountDownLatch latch = new CountDownLatch(1);

    executor.submit(() -> {
      LOG.info("[COMMAND] Removing image {}", image.hash());

      try {
        cmd("docker rmi %s", image.hash());
        latch.countDown();
      } catch (IOException | InterruptedException | ProcessExitException e) {
        LOG.error("Error during removeImage {}", image.hash(), e);
      } finally {
        LOG.info("[COMMAND] Done removing image {}", image.hash());
      }
    });

    return await(latch);
  }

  private ArrayList<String> cmd(final String format, final Object... args) throws IOException, InterruptedException, ProcessExitException {
    return cmd(String.format(format, args));
  }

  private ArrayList<String> cmd(final String cmd) throws IOException, InterruptedException, ProcessExitException {
    return CmdUtil.cmdDebug("/", cmd);
    // return CmdUtil.cmd(dir, cmd);
  }
}
