package nl.yogh.aerius.wui.builder.commands;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.LogMessage;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class LogBatchEvent extends SimpleGenericEvent<ArrayList<LogMessage>> {
  public LogBatchEvent(final ArrayList<LogMessage> c) {
    super(c);
  }
}
