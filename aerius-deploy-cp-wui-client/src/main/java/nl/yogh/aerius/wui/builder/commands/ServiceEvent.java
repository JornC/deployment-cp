package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ServiceEvent extends SimpleGenericEvent<ServiceInfo> {
  public ServiceEvent(final ServiceInfo type) {
    super(type);
  }
}
