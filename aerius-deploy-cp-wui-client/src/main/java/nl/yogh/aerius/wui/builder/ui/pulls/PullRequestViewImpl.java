package nl.yogh.aerius.wui.builder.ui.pulls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalEvent;
import nl.yogh.aerius.wui.builder.component.PullRequestControlPanel;
import nl.yogh.gwt.wui.widget.EventComposite;

public class PullRequestViewImpl extends EventComposite implements PullRequestView {
  private static final LandingViewImplUiBinder UI_BINDER = GWT.create(LandingViewImplUiBinder.class);

  interface LandingViewImplUiBinder extends UiBinder<Widget, PullRequestViewImpl> {}

  private final LandingViewImplEventBinder EVENT_BINDER = GWT.create(LandingViewImplEventBinder.class);

  interface LandingViewImplEventBinder extends EventBinder<PullRequestViewImpl> {}

  @UiField FlowPanel pullRequestPanel;

  @Inject
  public PullRequestViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @EventHandler
  public void onPullRequestRetrievalEvent(final PullRequestRetrievalEvent e) {
    for (final PullRequestInfo info : e.getValue()) {
      pullRequestPanel.add(new PullRequestControlPanel(info));
    }
  }

  @Override
  public void setPresenter(final Presenter presenter) {}

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }
}
