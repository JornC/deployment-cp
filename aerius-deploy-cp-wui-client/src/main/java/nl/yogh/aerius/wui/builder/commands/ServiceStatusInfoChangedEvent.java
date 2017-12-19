package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ServiceInfo;

public class ServiceStatusInfoChangedEvent extends ServiceEvent {
  public ServiceStatusInfoChangedEvent(final ServiceInfo info) {
    super(info);
  }
}
