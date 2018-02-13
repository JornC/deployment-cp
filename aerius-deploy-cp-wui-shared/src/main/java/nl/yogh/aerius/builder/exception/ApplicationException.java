package nl.yogh.aerius.builder.exception;

import java.io.Serializable;

public class ApplicationException extends Exception implements Serializable {
  private static final long serialVersionUID = 1482306365527497994L;

  public enum Reason {
    INTERNAL_ERROR(666);

    private final int errorCode;

    Reason(final int errorCode) {
      this.errorCode = errorCode;
    }

    public int getErrorCode() {
      return errorCode;
    }
  }

  private Reason reason;

  public ApplicationException() {}

  public ApplicationException(final Reason errorCode) {
    super();
    this.reason = errorCode;
  }

  public ApplicationException(final Reason errorCode, final String message) {
    super(message);
    this.reason = errorCode;
  }

  public Reason getReason() {
    return reason;
  }
}
