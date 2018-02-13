package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.wui.builder.commands.CompositionRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.CompositionRetrievalDeactivationCommand;

public abstract class CompositionUpdatePollingAgent extends PollingAgent<ArrayList<CompositionInfo>> implements CompositionUpdateDaemon {
  private static final int PULL_REPEAT_DELAY = 1000;

  protected final EventBus eventBus;

  public CompositionUpdatePollingAgent(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @EventHandler
  public void onCompositionRetrievalActivationCommand(final CompositionRetrievalActivationCommand c) {
    start();
  }

  @EventHandler
  public void onCompositionRetrievalDeactivationCommand(final CompositionRetrievalDeactivationCommand c) {
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
