package nl.yogh.aerius.wui.builder.ui.logs;

import javax.inject.Inject;

import nl.yogh.aerius.wui.builder.commands.LogRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.LogRetrievalDeactivationCommand;
import nl.yogh.aerius.wui.builder.ui.logs.LogView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;

public class LogActivity extends EventActivity<Presenter, LogView> implements Presenter {

  @Inject
  public LogActivity(final LogView view) {
    super(view);
  }

  @Override
  protected void onStart() {
    eventBus.fireEvent(new LogRetrievalActivationCommand());
  }

  @Override
  public void onStop() {
    eventBus.fireEvent(new LogRetrievalDeactivationCommand());
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
