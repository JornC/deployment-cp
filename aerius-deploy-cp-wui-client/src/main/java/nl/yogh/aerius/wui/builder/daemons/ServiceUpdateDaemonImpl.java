package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.ServiceStatusInfoChangedEvent;

public class ServiceUpdateDaemonImpl extends ServiceUpdatePollingAgent {
  interface ServiceUpdateDaemonImplEventBinder extends EventBinder<ServiceUpdateDaemonImpl> {}

  private final ServiceUpdateDaemonImplEventBinder EVENT_BINDER = GWT.create(ServiceUpdateDaemonImplEventBinder.class);

  private final PullRequestServiceAsync service;

  private long lastUpdate;

  @Inject
  public ServiceUpdateDaemonImpl(final EventBus eventBus, final PullRequestServiceAsync service) {
    super(eventBus);

    this.service = service;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  public void start(final long lastUpdate) {
    super.start();
  }

  @Override
  protected void fetch(final AsyncCallback<ArrayList<ServiceInfo>> callback) {
    service.getServiceUpdates(lastUpdate, callback);
    lastUpdate = new Date().getTime();
  }

  @Override
  protected void handleResult(final ArrayList<ServiceInfo> result) {
    if (!result.isEmpty()) {
      GWT.log("Got service updates: " + result.size());
    }

    for (final ServiceInfo info : result) {
      eventBus.fireEvent(new ServiceStatusInfoChangedEvent(info));
    }
  }
}
