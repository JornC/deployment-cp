package nl.yogh.aerius.wui.builder.commands;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ServicesRetrievalEvent extends SimpleGenericEvent<ArrayList<ServiceInfo>> {
  public ServicesRetrievalEvent(final ArrayList<ServiceInfo> value) {
    super(value);
  }
}
