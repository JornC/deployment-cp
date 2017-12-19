package nl.yogh.aerius.wui.builder.daemons;

import java.util.function.Consumer;

import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ResponseEvent<R, S> extends SimpleGenericEvent<R> {
  private final Consumer<S> consumer;
  private Runnable failure;

  public ResponseEvent(final R request, final Consumer<S> consumer) {
    super(request);

    this.consumer = consumer;
  }

  public ResponseEvent(final R request, final Consumer<S> consumer, final Runnable failure) {
    this(request, consumer);

    this.failure = failure;
  }

  public void respond(final S response) {
    consumer.accept(response);
  }

  public void fail() {
    if (failure != null) {
      failure.run();
    }
  }
}
