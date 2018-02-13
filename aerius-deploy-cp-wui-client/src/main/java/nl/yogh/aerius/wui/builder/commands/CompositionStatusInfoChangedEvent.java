package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.CompositionInfo;

public class CompositionStatusInfoChangedEvent extends CompositionEvent {
  public CompositionStatusInfoChangedEvent(final CompositionInfo info) {
    super(info);
  }
}
