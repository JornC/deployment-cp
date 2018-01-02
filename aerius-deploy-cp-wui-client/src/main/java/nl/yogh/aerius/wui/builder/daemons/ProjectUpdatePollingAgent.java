package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;

import com.google.web.bindery.event.shared.EventBus;

import nl.yogh.aerius.builder.domain.ProjectInfo;

public abstract class ProjectUpdatePollingAgent extends PollingAgent<ArrayList<ProjectInfo>> implements ProjectUpdateDaemon {
  private static final int PULL_REPEAT_DELAY = 1000;

  protected final EventBus eventBus;

  public ProjectUpdatePollingAgent(final EventBus eventBus) {
    this.eventBus = eventBus;
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
