package nl.yogh.aerius.wui.builder.ui.landing;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.yogh.gwt.wui.widget.EventComposite;

public class LandingViewImpl extends EventComposite implements LandingView {
  private static final LandingViewImplUiBinder UI_BINDER = GWT.create(LandingViewImplUiBinder.class);

  interface LandingViewImplUiBinder extends UiBinder<Widget, LandingViewImpl> {}

  private final LandingViewImplEventBinder EVENT_BINDER = GWT.create(LandingViewImplEventBinder.class);

  interface LandingViewImplEventBinder extends EventBinder<LandingViewImpl> {}

  @Inject
  public LandingViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  protected void onLoad() {}

  @Override
  public void setPresenter(final Presenter presenter) {}

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
