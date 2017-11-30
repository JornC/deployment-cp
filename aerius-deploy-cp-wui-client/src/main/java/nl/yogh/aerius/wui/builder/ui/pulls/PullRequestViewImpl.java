package nl.yogh.aerius.wui.builder.ui.pulls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.yogh.gwt.wui.widget.EventComposite;

public class PullRequestViewImpl extends EventComposite implements PullRequestView {
  private static final LandingViewImplUiBinder UI_BINDER = GWT.create(LandingViewImplUiBinder.class);

  interface LandingViewImplUiBinder extends UiBinder<Widget, PullRequestViewImpl> {}

  private final LandingViewImplEventBinder EVENT_BINDER = GWT.create(LandingViewImplEventBinder.class);

  interface LandingViewImplEventBinder extends EventBinder<PullRequestViewImpl> {}

  @Inject
  public PullRequestViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setPresenter(final Presenter presenter) {}

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
