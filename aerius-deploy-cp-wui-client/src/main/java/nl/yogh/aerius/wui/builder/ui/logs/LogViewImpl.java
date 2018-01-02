package nl.yogh.aerius.wui.builder.ui.logs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.LogMessage;
import nl.yogh.aerius.wui.builder.commands.LogBatchEvent;
import nl.yogh.aerius.wui.builder.component.LogMessageView;
import nl.yogh.gwt.wui.widget.EventComposite;

public class LogViewImpl extends EventComposite implements LogView {
  private static final LogViewImplUiBinder UI_BINDER = GWT.create(LogViewImplUiBinder.class);

  interface LogViewImplUiBinder extends UiBinder<Widget, LogViewImpl> {}

  interface LogViewImplEventBinder extends EventBinder<LogViewImpl> {}

  private final LogViewImplEventBinder EVENT_BINDER = GWT.create(LogViewImplEventBinder.class);

  @UiField FlowPanel logMessages;

  @Inject
  public LogViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  protected void onLoad() {}

  @Override
  public void setPresenter(final Presenter presenter) {}

  @EventHandler
  public void onLogBatchEvent(final LogBatchEvent e) {
    for (final LogMessage msg : e.getValue()) {
      logMessages.add(new LogMessageView(msg));
    }

    new Timer() {
      @Override
      public void run() {
        if (!e.getValue().isEmpty()) {
          logMessages.getWidget(logMessages.getWidgetCount() - 1).getElement().scrollIntoView();
        }
      };
    }.schedule(100);
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
