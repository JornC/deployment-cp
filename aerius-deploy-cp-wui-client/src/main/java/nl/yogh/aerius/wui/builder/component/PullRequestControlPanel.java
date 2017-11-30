package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.gwt.wui.widget.EventComposite;

public class PullRequestControlPanel extends EventComposite {
  private static final PullRequestControlPanelUiBinder UI_BINDER = GWT.create(PullRequestControlPanelUiBinder.class);

  interface PullRequestControlPanelUiBinder extends UiBinder<Widget, PullRequestControlPanel> {}

  @UiField Label idxField;
  @UiField Label titleField;
  @UiField Label authorName;

  public PullRequestControlPanel(final PullRequestInfo info) {
    initWidget(UI_BINDER.createAndBindUi(this));

    idxField.setText("#" + info.getPullRequestIdx());
    titleField.setText(info.getTitle());
    authorName.setText(info.getAuthorName());
  }
}
