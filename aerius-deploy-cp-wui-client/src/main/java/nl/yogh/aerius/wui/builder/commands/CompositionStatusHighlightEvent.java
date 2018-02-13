package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.CompositionInfo;

public class CompositionStatusHighlightEvent extends CompositionEvent {
  private final boolean highlight;

  public CompositionStatusHighlightEvent(final CompositionInfo info, final boolean highlight) {
    super(info);
    this.highlight = highlight;
  }

  public boolean isHighlight() {
    return highlight;
  }
}
