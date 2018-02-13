package nl.yogh.aerius.server.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;

public class ProjectUpdateRepositoryFactory {
  private static final Logger LOG = LoggerFactory.getLogger(ProjectUpdateRepositoryFactory.class);

  private static TimestampedMultiMap<CompositionInfo> projectUpdateRepository;

  public static void init() {
    synchronized (ProjectUpdateRepositoryFactory.class) {
      if (projectUpdateRepository == null) {
        projectUpdateRepository = new TimestampedMultiMap<>();
      }
    }

    LOG.info("ProjectUpdateRepository initialized.");
  }

  public static TimestampedMultiMap<CompositionInfo> getInstance() {
    if (projectUpdateRepository == null) {
      init();
    }

    return projectUpdateRepository;
  }
}
