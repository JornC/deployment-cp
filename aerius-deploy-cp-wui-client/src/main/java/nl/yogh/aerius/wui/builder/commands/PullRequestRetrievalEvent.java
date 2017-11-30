package nl.yogh.aerius.wui.builder.commands;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class PullRequestRetrievalEvent extends SimpleGenericEvent<ArrayList<PullRequestInfo>> {
  public PullRequestRetrievalEvent(final ArrayList<PullRequestInfo> value) {
    super(value);
  }
}
