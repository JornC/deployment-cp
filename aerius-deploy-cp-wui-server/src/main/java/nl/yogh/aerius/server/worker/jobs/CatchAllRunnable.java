package nl.yogh.aerius.server.worker.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatchAllRunnable implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(CatchAllRunnable.class);

  private final Runnable delegate;

  public CatchAllRunnable(final Runnable delegate) {
    this.delegate = delegate;
  }

  @Override
  public void run() {
    try {
      delegate.run();
    } catch (final Exception e) {
      LOG.error("Exception while running task {}.", delegate.getClass().getSimpleName(), e);
    }
  }

  public static Runnable wrap(final Runnable delegate) {
    return new CatchAllRunnable(delegate);
  }
}
