package nl.yogh.aerius.wui.builder.daemons;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalDeactivationCommand;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalEvent;

public class PullRetrievalDaemonImpl implements PullRetrievalDaemon {
  interface PullRetrievalDaemonImplEventBinder extends EventBinder<PullRetrievalDaemonImpl> {}

  private final PullRetrievalDaemonImplEventBinder eventBinder = GWT.create(PullRetrievalDaemonImplEventBinder.class);
  private final PullRequestServiceAsync service;
  private final EventBus eventBus;

  @Inject
  public PullRetrievalDaemonImpl(final EventBus eventBus, final PullRequestServiceAsync service) {
    this.eventBus = eventBus;
    this.service = service;

    eventBinder.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onPullRequestRetrievalActivationCommand(final PullRequestRetrievalActivationCommand c) {
    service.getPullRequests(r -> eventBus.fireEvent(new PullRequestRetrievalEvent(r)));
  }

  @EventHandler
  public void onPullRequestRetrievalDeactivationCommand(final PullRequestRetrievalDeactivationCommand c) {

  }
}
