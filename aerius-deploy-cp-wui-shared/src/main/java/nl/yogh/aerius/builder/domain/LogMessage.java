package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LogMessage implements Serializable, IsSerializable {
  private static final long serialVersionUID = -1276391541497786199L;

  private long time;
  private String message;

  public LogMessage() {}

  public static LogMessage create() {
    return new LogMessage();
  }

  public long time() {
    return time;
  }

  public LogMessage time(final long time) {
    this.time = time;
    return this;
  }

  public String message() {
    return message;
  }

  public LogMessage message(final String message) {
    this.message = message;
    return this;
  }
}
