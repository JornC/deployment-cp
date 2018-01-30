package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.wui.builder.commands.ProjectRetrievalActivationCommand;
import nl.yogh.aerius.wui.builder.commands.ProjectRetrievalDeactivationCommand;

public abstract class ProjectUpdatePollingAgent extends PollingAgent<ArrayList<ProjectInfo>> implements ProjectUpdateDaemon {
  private static final int PULL_REPEAT_DELAY = 1000;

  protected final EventBus eventBus;

  public ProjectUpdatePollingAgent(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @EventHandler
  public void onProjectRetrievalActivationCommand(final ProjectRetrievalActivationCommand c) {
    start();
  }

  @EventHandler
  public void onProjectRetrievalDeactivationCommand(final ProjectRetrievalDeactivationCommand c) {
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
