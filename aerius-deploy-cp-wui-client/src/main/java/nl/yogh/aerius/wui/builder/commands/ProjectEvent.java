package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ProjectEvent extends SimpleGenericEvent<ProjectInfo> {
  public ProjectEvent(final ProjectInfo type) {
    super(type);
  }
}
