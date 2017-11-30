package nl.yogh.aerius.wui.builder.ui.pulls;

import javax.inject.Inject;

import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalDeactivationCommand;
import nl.yogh.aerius.wui.builder.ui.pulls.PullRequestView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;

public class PullRequestActivity extends EventActivity<Presenter, PullRequestView> implements Presenter {

  @Inject
  public PullRequestActivity(final PullRequestView view) {
    super(view);
  }

  @Override
  protected void onStart() {
    eventBus.fireEvent(new PullRequestRetrievalActivationCommand());
  }

  @Override
  public void onStop() {
    eventBus.fireEvent(new PullRequestRetrievalDeactivationCommand());
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
