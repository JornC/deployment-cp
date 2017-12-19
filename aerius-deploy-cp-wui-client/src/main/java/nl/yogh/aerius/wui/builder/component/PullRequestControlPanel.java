package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.wui.builder.commands.PullRequestStatusInfoChangedEvent;
import nl.yogh.gwt.wui.widget.FieldedEventSimplePanel;

public class PullRequestControlPanel extends FieldedEventSimplePanel {
  static interface PullRequestControlPanelEventBinder extends EventBinder<PullRequestControlPanel> {}

  private static final PullRequestControlPanelEventBinder EVENT_BINDER = GWT.create(PullRequestControlPanelEventBinder.class);

  interface PullRequestControlPanelUiBinder extends UiBinder<Widget, PullRequestControlPanel> {}

  private static final PullRequestControlPanelUiBinder UI_BINDER = GWT.create(PullRequestControlPanelUiBinder.class);

  public interface CustomStyle extends CssResource {
    String busy();
  }

  @UiField(provided = true) PullRequestInfo pull;

  @UiField FlowPanel panel;
  @UiField CustomStyle style;

  private HandlerRegistration eventRegistration;

  public PullRequestControlPanel(final EventBus eventBus, final PullRequestInfo pull) {
    super(eventBus);

    initPull(pull);
  }

  @EventHandler
  public void onPullRequestStatusInfoChangedEvent(final PullRequestStatusInfoChangedEvent e) {
    if (!e.getValue().idx().equals(pull.idx())) {
      return;
    }

    // If the pull idx matches, but the root hash differs (PR was updated) then reset
    if (!pull.hash().equals(e.getValue().hash())) {
      GWT.log("Root hash changed. Updating.");
      initPull(e.getValue());
      return; // Readability
    }

    // If the pull was busy, and now isn't, or it wasn't busy, but now is, then set busy status
    if (pull.isBusy() != e.getValue().isBusy()) {
      initPull(e.getValue());
    }
  }

  private void initPull(final PullRequestInfo pull) {
    this.pull = pull;

    setWidget(UI_BINDER.createAndBindUi(this));

    setBusy(pull.isBusy());
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    eventRegistration = EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @Override
  protected void onUnload() {
    eventRegistration.removeHandler();
  }

  private void setBusy(final boolean busy) {
    panel.setStyleName(style.busy(), busy);
  }

  public PullRequestInfo getPull() {
    return pull;
  }
}
