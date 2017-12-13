package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.wui.builder.commands.PullRequestStatusInfoChangedEvent;
import nl.yogh.gwt.wui.widget.FieldedEventSimplePanel;

public class PullRequestControlPanel extends FieldedEventSimplePanel {
  private static final PullRequestControlPanelUiBinder UI_BINDER = GWT.create(PullRequestControlPanelUiBinder.class);

  interface PullRequestControlPanelUiBinder extends UiBinder<Widget, PullRequestControlPanel> {}

  public interface CustomStyle extends CssResource {
    String busy();
  }

  @UiField(provided = true) PullRequestInfo pull;

  @UiField FlowPanel panel;
  @UiField CustomStyle style;

  public PullRequestControlPanel(final EventBus eventBus, final PullRequestInfo pull) {
    super(eventBus);

    initPull(pull);
  }

  @EventHandler
  public void onPullRequestStatusInfoChangedEvent(final PullRequestStatusInfoChangedEvent e) {
    initPull(e.getValue());
  }

  private void initPull(final PullRequestInfo pull) {
    this.pull = pull;

    setWidget(UI_BINDER.createAndBindUi(this));

    setBusy(pull.isBusy());
  }

  private void setBusy(final boolean busy) {
    panel.setStyleName(style.busy(), busy);
  }

  public PullRequestInfo getPull() {
    return pull;
  }
}
