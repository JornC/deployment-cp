package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.ProductActionCommand;
import nl.yogh.aerius.wui.builder.commands.ProductStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalEvent;

public class PullRetrievalDaemonImpl extends PullRetrievalPollingAgent {
  interface PullRetrievalDaemonImplEventBinder extends EventBinder<PullRetrievalDaemonImpl> {}

  private final PullRetrievalDaemonImplEventBinder EVENT_BINDER = GWT.create(PullRetrievalDaemonImplEventBinder.class);

  private final PullRequestServiceAsync service;

  @Inject
  public PullRetrievalDaemonImpl(final EventBus eventBus, final PullRequestServiceAsync service) {
    super(eventBus);

    this.service = service;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onProductActionCommand(final ProductActionCommand c) {
    service.doAction(c.getType(), c.getAction(), c.getValue(), r -> eventBus.fireEvent(new ProductStatusInfoChangedEvent(r)));
  }

  @Override
  protected void handleResult(final ArrayList<PullRequestInfo> c) {
    eventBus.fireEvent(new PullRequestRetrievalEvent(c));
  }

  @Override
  protected void fetch(final AsyncCallback<ArrayList<PullRequestInfo>> callback) {
    service.getPullRequests(callback);
  }
}
