package nl.yogh.aerius.wui.builder.daemons;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

import nl.yogh.aerius.builder.service.ApplicationServiceAsync;
import nl.yogh.aerius.wui.util.UglyDuckling;
import nl.yogh.gwt.wui.daemon.DaemonBootstrapperImpl;
import nl.yogh.gwt.wui.daemon.ExceptionDaemon;
import nl.yogh.gwt.wui.dev.DevelopmentObserver;
import nl.yogh.gwt.wui.service.AppAsyncCallback;

public class BuilderDaemonBootstrapper extends DaemonBootstrapperImpl {
  @Inject PullRequestRetrievalDaemon pullRetrievalDaemon;

  @Inject ContainerRetrievalDaemon containerRetrievalDaemon;

  @Inject LogRetrievalDaemon logRetrievalDaemon;

  private final ApplicationServiceAsync service;

  @Inject
  public BuilderDaemonBootstrapper(final ApplicationServiceAsync service, final ExceptionDaemon exceptionDaemon, final DevelopmentObserver observer) {
    super(exceptionDaemon, observer);

    this.service = service;
  }

  @Override
  public void init(final Runnable complete) {
    super.init(complete);

    GWT.log("Getting application config");
    service.getApplicationConfig(AppAsyncCallback.create(v -> {
      GWT.log("Gots it");
      UglyDuckling.compositions(v);
      complete.run();
    }));

    // complete.run();
  }

  @Override
  protected boolean autoComplete() {
    return false;
  }
}
