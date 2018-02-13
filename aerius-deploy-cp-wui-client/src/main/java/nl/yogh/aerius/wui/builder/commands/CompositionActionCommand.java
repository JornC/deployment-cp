package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.CompositionDeploymentAction;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class CompositionActionCommand extends SimpleGenericEvent<CompositionInfo> {
  private final String idx;

  private final CompositionDeploymentAction action;

  public CompositionActionCommand(final String idx, final CompositionInfo info, final CompositionDeploymentAction action) {
    super(info);
    this.idx = idx;
    this.action = action;
  }

  public String getIdx() {
    return idx;
  }

  public CompositionDeploymentAction getAction() {
    return action;
  }
}
