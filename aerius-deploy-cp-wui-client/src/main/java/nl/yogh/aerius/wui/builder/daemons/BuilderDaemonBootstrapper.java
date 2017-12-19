package nl.yogh.aerius.wui.builder.daemons;

import com.google.inject.Inject;

import nl.yogh.gwt.wui.daemon.DaemonBootstrapperImpl;
import nl.yogh.gwt.wui.daemon.ExceptionDaemon;
import nl.yogh.gwt.wui.dev.DevelopmentObserver;

public class BuilderDaemonBootstrapper extends DaemonBootstrapperImpl {
  @Inject PullRequestRetrievalDaemon pullRetrievalDaemon;

  @Inject ContainerRetrievalDaemon containerRetrievalDaemon;

  @Inject
  public BuilderDaemonBootstrapper(final ExceptionDaemon exceptionDaemon, final DevelopmentObserver observer) {
    super(exceptionDaemon, observer);
  }
}
