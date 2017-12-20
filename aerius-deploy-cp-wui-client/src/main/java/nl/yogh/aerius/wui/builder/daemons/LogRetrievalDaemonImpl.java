package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.yogh.aerius.builder.domain.LogMessage;
import nl.yogh.aerius.builder.service.LogServiceAsync;
import nl.yogh.aerius.wui.builder.commands.LogBatchEvent;

public class LogRetrievalDaemonImpl extends LogRetrievalPollingAgent {
  interface LogRetrievalDaemonImplEventBinder extends EventBinder<LogRetrievalDaemonImpl> {}

  private final LogRetrievalDaemonImplEventBinder EVENT_BINDER = GWT.create(LogRetrievalDaemonImplEventBinder.class);

  private final LogServiceAsync service;

  private long lastUpdate;

  @Inject
  public LogRetrievalDaemonImpl(final EventBus eventBus, final LogServiceAsync service) {
    super(eventBus);

    this.service = service;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @Override
  public void start() {
    lastUpdate = 0;

    super.start();
  }

  @Override
  protected void handleResult(final ArrayList<LogMessage> c) {
    eventBus.fireEvent(new LogBatchEvent(c));
  }

  @Override
  protected void fetch(final AsyncCallback<ArrayList<LogMessage>> callback) {
    service.getLogMessages(lastUpdate, callback);
    lastUpdate = new Date().getTime();
  }

}
