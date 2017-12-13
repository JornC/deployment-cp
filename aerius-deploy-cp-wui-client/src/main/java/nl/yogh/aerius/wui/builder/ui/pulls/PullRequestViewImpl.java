package nl.yogh.aerius.wui.builder.ui.pulls;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

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
  public void insertOrUpdatePullRequest(final PullRequestInfo info) {
    final String idx = info.idx();

    GWT.log("INSERT/UPDATE: " + idx);

    if (!pullRequestMap.containsKey(idx)) {
      final PullRequestControlPanel newPanel = new PullRequestControlPanel(eventBus, info);
      pullRequestPanel.add(newPanel);
      pullRequestMap.put(idx, newPanel);
    } else {
      final Widget oldPanel = pullRequestMap.get(idx);
      final PullRequestControlPanel newPanel = new PullRequestControlPanel(eventBus, info);

      final int updateIdx = pullRequestPanel.getWidgetIndex(oldPanel);
      pullRequestPanel.remove(updateIdx);
      pullRequestPanel.insert(newPanel, updateIdx);
      pullRequestMap.put(idx, newPanel);
    }
  }
}
