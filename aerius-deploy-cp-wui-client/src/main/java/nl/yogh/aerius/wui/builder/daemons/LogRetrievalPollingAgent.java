package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.LogMessage;
import nl.yogh.aerius.wui.builder.commands.LogRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.LogRetrievalDeactivationCommand;

public abstract class LogRetrievalPollingAgent extends PollingAgent<ArrayList<LogMessage>> implements LogRetrievalDaemon {
  private static final int LOG_REPEAT_DELAY = 500;

  protected final EventBus eventBus;

  public LogRetrievalPollingAgent(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @EventHandler
  public void onPullRequestRetrievalActivationCommand(final LogRetrievalActivationCommand c) {
    start();
  }

  @EventHandler
  public void onPullRequestRetrievalDeactivationCommand(final LogRetrievalDeactivationCommand c) {
    stop();
  }

  @Override
  protected boolean onFailure(final Throwable caught) {
    return false;
  }

  @Override
  protected int getPollRepeatDelay() {
    return LOG_REPEAT_DELAY;
  }
}
