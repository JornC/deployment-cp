package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProjectDeploymentAction;
import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ProjectActionCommand extends SimpleGenericEvent<ProjectInfo> {
  private final String idx;

  private final ProjectDeploymentAction action;

  public ProjectActionCommand(final String idx, final ProjectInfo info, final ProjectDeploymentAction action) {
    super(info);
    this.idx = idx;
    this.action = action;
  }

  public String getIdx() {
    return idx;
  }

  public ProjectDeploymentAction getAction() {
    return action;
  }
}
