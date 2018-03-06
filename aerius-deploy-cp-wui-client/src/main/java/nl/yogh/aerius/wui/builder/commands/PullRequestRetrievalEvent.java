package nl.yogh.aerius.wui.builder.commands;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.CommitInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class PullRequestRetrievalEvent extends SimpleGenericEvent<ArrayList<CommitInfo>> {
  public PullRequestRetrievalEvent(final ArrayList<CommitInfo> value) {
    super(value);
  }
}
