package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.ProjectStatusInfoChangedEvent;

public class ProjectUpdateDaemonImpl extends ProjectUpdatePollingAgent {
  interface ProjectUpdateDaemonImplEventBinder extends EventBinder<ProjectUpdateDaemonImpl> {}

  private final ProjectUpdateDaemonImplEventBinder EVENT_BINDER = GWT.create(ProjectUpdateDaemonImplEventBinder.class);

  private final PullRequestServiceAsync service;

  private long lastUpdate;

  @Inject
  public ProjectUpdateDaemonImpl(final EventBus eventBus, final PullRequestServiceAsync service) {
    super(eventBus);

    this.service = service;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  public void start(final long lastUpdate) {
    this.lastUpdate = lastUpdate;

    super.start();
  }

  @Override
  protected void fetch(final AsyncCallback<ArrayList<ProjectInfo>> callback) {
    service.getProductUpdates(lastUpdate, callback);
    lastUpdate = new Date().getTime();
  }

  @Override
  protected void handleResult(final ArrayList<ProjectInfo> result) {
    if (!result.isEmpty()) {
      GWT.log("Got project updates: " + result.size());
    }

    for (final ProjectInfo info : result) {
      eventBus.fireEvent(new ProjectStatusInfoChangedEvent(info));
    }
  }
}
