package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalDeactivationCommand;

public abstract class PullRequestRetrievalPollingAgent extends PollingAgent<ArrayList<PullRequestInfo>> implements PullRequestRetrievalDaemon {
  private static final int PULL_REPEAT_DELAY = 1000;

  protected final EventBus eventBus;

  public PullRequestRetrievalPollingAgent(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @EventHandler
  public void onPullRequestRetrievalActivationCommand(final PullRequestRetrievalActivationCommand c) {
    start();
  }

  @EventHandler
  public void onPullRequestRetrievalDeactivationCommand(final PullRequestRetrievalDeactivationCommand c) {
    stop();
  }

  @Override
  protected boolean onFailure(final Throwable caught) {
    return false;
  }

  @Override
  protected int getPollRepeatDelay() {
    return PULL_REPEAT_DELAY;
  }
}
