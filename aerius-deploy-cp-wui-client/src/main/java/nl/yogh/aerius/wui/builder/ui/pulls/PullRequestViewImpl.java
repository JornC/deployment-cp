package nl.yogh.aerius.wui.builder.ui.pulls;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.wui.builder.component.PullRequestControlPanel;
import nl.yogh.gwt.wui.widget.EventComposite;

public class PullRequestViewImpl extends EventComposite implements PullRequestView {
  private static final LandingViewImplUiBinder UI_BINDER = GWT.create(LandingViewImplUiBinder.class);

  interface LandingViewImplUiBinder extends UiBinder<Widget, PullRequestViewImpl> {}

  @UiField FlowPanel pullRequestPanel;

  private final HashMap<String, PullRequestControlPanel> pullRequestMap = new HashMap<>();

  @Inject
  public PullRequestViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public void setPresenter(final Presenter presenter) {}

  @Override
  public void insertPullRequest(final PullRequestInfo info) {
    final String idx = info.idx();

    if (!pullRequestMap.containsKey(idx)) {
      final PullRequestControlPanel newPanel = new PullRequestControlPanel(eventBus, info);
      pullRequestPanel.add(newPanel);
      pullRequestMap.put(idx, newPanel);
    } else {
      GWT.log("BUG!");
    }
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    GWT.log("ViewImpl got event bus!");
  }

  @Override
  public void removePullRequest(final String hash) {
    final PullRequestControlPanel pull = pullRequestMap.get(hash);
    if (pull == null) {
      return;
    }

    pull.removeFromParent();
  }
}
