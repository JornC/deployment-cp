package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProjectInfo;

public class ProjectStatusHighlightEvent extends ProjectEvent {
  private final boolean highlight;

  public ProjectStatusHighlightEvent(final ProjectInfo info, final boolean highlight) {
    super(info);
    this.highlight = highlight;
  }

  public boolean isHighlight() {
    return highlight;
  }
}
