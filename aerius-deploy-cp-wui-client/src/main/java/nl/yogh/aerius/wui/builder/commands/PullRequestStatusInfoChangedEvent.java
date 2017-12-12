package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class PullRequestStatusInfoChangedEvent extends SimpleGenericEvent<PullRequestInfo> {
  public PullRequestStatusInfoChangedEvent(final PullRequestInfo info) {
    super(info);
  }
}
