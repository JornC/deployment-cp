package nl.yogh.aerius.wui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.gwt.wui.event.NotificationEvent;
import nl.yogh.gwt.wui.widget.HasEventBus;

public class NotificationPanel extends FlowPanel implements HasEventBus {
  private final NotificationPanelEventBinder EVENT_BINDER = GWT.create(NotificationPanelEventBinder.class);

  interface NotificationPanelEventBinder extends EventBinder<NotificationPanel> {}

  @Override
  public void setEventBus(final EventBus eventBus) {
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onNotification(final NotificationEvent event) {
    add(new NotificationWidget(event.getValue()));
  }
}
