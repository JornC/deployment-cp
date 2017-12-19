package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectType;
import nl.yogh.aerius.builder.service.ProjectDeploymentAction;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ProjectActionCommand extends SimpleGenericEvent<ProjectInfo> {
  private final String idx;

  private final ProjectType type;
  private final ProjectDeploymentAction action;

  public ProjectActionCommand(final String idx, final ProjectInfo info, final ProjectType type, final ProjectDeploymentAction action) {
    super(info);
    this.idx = idx;
    this.type = type;
    this.action = action;
  }

  public String getIdx() {
    return idx;
  }

  public ProjectType getType() {
    return type;
  }

  public ProjectDeploymentAction getAction() {
    return action;
  }
}
