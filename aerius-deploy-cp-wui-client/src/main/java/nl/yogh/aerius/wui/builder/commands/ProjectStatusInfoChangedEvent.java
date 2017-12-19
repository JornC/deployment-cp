package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProjectInfo;

public class ProjectStatusInfoChangedEvent extends ProjectEvent {
  public ProjectStatusInfoChangedEvent(final ProjectInfo info) {
    super(info);
  }
}
