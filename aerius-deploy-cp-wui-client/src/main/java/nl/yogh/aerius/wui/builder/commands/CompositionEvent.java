package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class CompositionEvent extends SimpleGenericEvent<CompositionInfo> {
  public CompositionEvent(final CompositionInfo type) {
    super(type);
  }
}
