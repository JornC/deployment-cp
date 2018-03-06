package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.CommitInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class PullRequestStatusInfoChangedEvent extends SimpleGenericEvent<CommitInfo> {
  public PullRequestStatusInfoChangedEvent(final CommitInfo info) {
    super(info);
  }
}
