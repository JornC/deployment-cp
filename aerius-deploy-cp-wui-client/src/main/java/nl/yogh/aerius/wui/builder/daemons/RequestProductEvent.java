package nl.yogh.aerius.wui.builder.daemons;

import java.util.function.Consumer;

import nl.yogh.aerius.builder.domain.CompositionInfo;

public class RequestProductEvent extends ResponseEvent<String, CompositionInfo> {
  public RequestProductEvent(final String request, final Consumer<CompositionInfo> consumer) {
    super(request, consumer);
  }
}
