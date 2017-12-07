package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.gwt.wui.widget.FieldedEventComposite;

public class PullRequestControlPanel extends FieldedEventComposite {
  private static final PullRequestControlPanelUiBinder UI_BINDER = GWT.create(PullRequestControlPanelUiBinder.class);

  interface PullRequestControlPanelUiBinder extends UiBinder<Widget, PullRequestControlPanel> {}

  @UiField(provided = true) PullRequestInfo pull;

  public PullRequestControlPanel(final EventBus eventBus, final PullRequestInfo pull) {
    super(eventBus);
    this.pull = pull;

    initWidget(UI_BINDER.createAndBindUi(this));
  }
}
