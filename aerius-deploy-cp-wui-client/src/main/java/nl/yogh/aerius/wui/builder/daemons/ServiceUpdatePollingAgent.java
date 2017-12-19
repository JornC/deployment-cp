package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;

import com.google.web.bindery.event.shared.EventBus;

import nl.yogh.aerius.builder.domain.ServiceInfo;

public abstract class ServiceUpdatePollingAgent extends PollingAgent<ArrayList<ServiceInfo>> implements ServiceUpdateDaemon {
  private static final int PULL_REPEAT_DELAY = 1000;

  protected final EventBus eventBus;

  public ServiceUpdatePollingAgent(final EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  protected int getPollRepeatDelay() {
    return PULL_REPEAT_DELAY;
  }
}
