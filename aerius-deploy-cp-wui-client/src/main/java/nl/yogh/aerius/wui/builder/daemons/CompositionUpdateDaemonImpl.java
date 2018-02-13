package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.CompositionStatusInfoChangedEvent;

public class CompositionUpdateDaemonImpl extends CompositionUpdatePollingAgent {
  interface ProjectUpdateDaemonImplEventBinder extends EventBinder<CompositionUpdateDaemonImpl> {}

  private final ProjectUpdateDaemonImplEventBinder EVENT_BINDER = GWT.create(ProjectUpdateDaemonImplEventBinder.class);

  private final PullRequestServiceAsync service;

  private long lastUpdate;

  @Inject
  public CompositionUpdateDaemonImpl(final EventBus eventBus, final PullRequestServiceAsync service) {
    super(eventBus);

    this.service = service;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  public void start(final long lastUpdate) {
    this.lastUpdate = lastUpdate;

    super.start();
  }

  @Override
  protected void fetch(final AsyncCallback<ArrayList<CompositionInfo>> callback) {
    service.getProductUpdates(lastUpdate, callback);
    lastUpdate = new Date().getTime();
  }

  @Override
  protected void handleResult(final ArrayList<CompositionInfo> result) {
    if (!result.isEmpty()) {
      GWT.log("Got project updates: " + result.size());
    }

    for (final CompositionInfo info : result) {
      eventBus.fireEvent(new CompositionStatusInfoChangedEvent(info));
    }
  }
}
