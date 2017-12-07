package nl.yogh.aerius.wui.builder.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.wui.ApplicationRootView;
import nl.yogh.aerius.wui.builder.component.PlaceNavigation;
import nl.yogh.aerius.wui.builder.place.LandingPlace;
import nl.yogh.aerius.wui.i18n.M;
import nl.yogh.aerius.wui.resources.R;
import nl.yogh.aerius.wui.widget.NotificationPanel;
import nl.yogh.gwt.wui.event.PlaceChangeEvent;
import nl.yogh.gwt.wui.place.PlaceController;
import nl.yogh.gwt.wui.widget.EventComposite;
import nl.yogh.gwt.wui.widget.HeadingWidget;

public class ApplicationRootViewImpl extends EventComposite implements ApplicationRootView {
  private static final ApplicationRootViewImplUiBinder UI_BINDER = GWT.create(ApplicationRootViewImplUiBinder.class);

  interface ApplicationRootViewImplUiBinder extends UiBinder<Widget, ApplicationRootViewImpl> {}

  private final ApplicationRootViewImplEventBinder EVENT_BINDER = GWT.create(ApplicationRootViewImplEventBinder.class);

  interface ApplicationRootViewImplEventBinder extends EventBinder<ApplicationRootViewImpl> {}

  @UiField HeadingWidget placeTitle;

  @UiField SimplePanel contentPanel;

  @UiField(provided = true) NotificationPanel notificationPanel;

  @SuppressWarnings("rawtypes") @UiField(provided = true) PlaceNavigation placeNavigation;

  private final PlaceController placeController;

  @Inject
  @SuppressWarnings("rawtypes")
  public ApplicationRootViewImpl(final EventBus eventBus, final NotificationPanel notificationPanel, final PlaceNavigation placeNavigation,
      final PlaceController placeController) {
    this.notificationPanel = notificationPanel;
    this.placeNavigation = placeNavigation;
    this.placeController = placeController;

    initWidget(UI_BINDER.createAndBindUi(this));

    super.setEventBus(eventBus, notificationPanel);
    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onPlaceChange(final PlaceChangeEvent e) {
    final String clazz = e.getValue().getClass().getSimpleName();

    placeTitle.setText(M.messages().placeTitle(clazz));
    placeTitle.getElement().getStyle().setBackgroundColor(R.colors().placeBackgroundColor(clazz));
    placeTitle.getElement().getStyle().setColor(R.colors().placeFontColor(clazz));
  }

  @UiHandler("landingHeading")
  public void onLandingClick(final ClickEvent e) {
    placeController.goTo(new LandingPlace());
  }

  @Override
  public void setWidget(final IsWidget w) {
    contentPanel.setWidget(w);
  }
}
