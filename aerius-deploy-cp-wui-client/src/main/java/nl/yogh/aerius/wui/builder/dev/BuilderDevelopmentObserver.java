package nl.yogh.aerius.wui.builder.dev;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;

import nl.yogh.gwt.wui.command.AbstractCommand;
import nl.yogh.gwt.wui.command.PlaceChangeCommand;
import nl.yogh.gwt.wui.command.SimpleGenericCommand;
import nl.yogh.gwt.wui.dev.AbstractLogger;
import nl.yogh.gwt.wui.dev.DevelopmentObserver;
import nl.yogh.gwt.wui.event.NotificationEvent;
import nl.yogh.gwt.wui.event.PlaceChangeEvent;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

@Singleton
public class BuilderDevelopmentObserver extends AbstractLogger implements DevelopmentObserver {
  interface DevelopmentObserverMonitorImplEventBinder extends EventBinder<BuilderDevelopmentObserver> {}

  private final DevelopmentObserverMonitorImplEventBinder EVENT_BINDER = GWT.create(DevelopmentObserverMonitorImplEventBinder.class);
  private final EventBus eventBus;

  @Inject
  public BuilderDevelopmentObserver(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void init() {
    EVENT_BINDER.bindEventHandlers(this, eventBus);

    log("initialized development observer.");
  }

  @EventHandler(handles = { PlaceChangeCommand.class })
  public void onSimpleGenericCommand(@SuppressWarnings("rawtypes") final SimpleGenericCommand e) {
    log(e.getClass().getSimpleName(), e.getValue());
  }

  // @EventHandler(handles = {})
  public void onSimpleGenericCommand(@SuppressWarnings("rawtypes") final AbstractCommand e) {
    log(e.getClass().getSimpleName());
  }

  // @EventHandler(handles = {})
  public void onSimpleGenericCommand(final GenericEvent e) {
    log(e.getClass().getSimpleName());
  }

  @EventHandler(handles = { NotificationEvent.class, PlaceChangeEvent.class })
  public void onSimpleGenericEvent(@SuppressWarnings("rawtypes") final SimpleGenericEvent e) {
    log(e.getClass().getSimpleName(), e.getValue());
    brbr();
  }
}
