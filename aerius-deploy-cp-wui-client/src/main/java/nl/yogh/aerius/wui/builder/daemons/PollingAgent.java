package nl.yogh.aerius.wui.builder.daemons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import nl.yogh.gwt.wui.service.AppAsyncCallback;

public abstract class PollingAgent<T> {
  private static final int DEFAULT_REPEAT_DELAY = 2000;

  private final Timer timer;

  public PollingAgent() {
    timer = new Timer() {
      @Override
      public void run() {
        fetch();
      }
    };
  }

  protected void start() {
    if (!timer.isRunning()) {
      fetch();
    } else {
      GWT.log("[WARNING] PollingAgent: Listener started while already running, which should not have happened. (bad caller)");
    }
  }

  protected void stop() {
    timer.cancel();
  }

  protected void fetch() {
    fetch(new AppAsyncCallback<T>() {
      @Override
      public void onSuccess(final T result) {
        if (result != null) {
          handleResult(result);
        }

        reschedule();
      }

      @Override
      public void onFailure(final Throwable caught) {
        super.onFailure(caught);
        reschedule();
      }

      private void reschedule() {
        timer.schedule(getPollRepeatDelay());

      }
    });
  }

  protected abstract void handleResult(final T c);

  protected abstract void fetch(final AsyncCallback<T> callback);

  protected int getPollRepeatDelay() {
    return DEFAULT_REPEAT_DELAY;
  }
}
