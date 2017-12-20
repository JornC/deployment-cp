package nl.yogh.aerius.server.logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.LogMessage;
import nl.yogh.aerius.server.startup.TimestampedMultiMap;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class ApplicationAppender extends AppenderBase<LoggingEvent> {
  private static final Logger LOG = LoggerFactory.getLogger(ApplicationAppender.class);

  private static final long CLEAN_INTERVAL = 15;
  private static final long CACHE_MILLISECONDS = CLEAN_INTERVAL * 60 * 1000;

  private static ApplicationAppender INSTANCE;

  private final TimestampedMultiMap<LoggingEvent> messages = new TimestampedMultiMap<>();

  private final ScheduledExecutorService cleanExecutor;

  public ApplicationAppender() {
    if (INSTANCE != null) {
      LOG.error("ApplicationAppender is apparently being created multiple times..");
    }

    INSTANCE = this;

    cleanExecutor = Executors.newSingleThreadScheduledExecutor();
    cleanExecutor.scheduleWithFixedDelay(() -> cleanPastEvents(), CLEAN_INTERVAL, CLEAN_INTERVAL, TimeUnit.MINUTES);

    LOG.info("ApplicationAppender initialized.");
  }

  private void cleanPastEvents() {
    final long clearBefore = new Date().getTime() - CACHE_MILLISECONDS;

    synchronized (messages) {
      messages.keySet().removeIf(o -> o < clearBefore);
    }
  }

  @Override
  protected void append(final LoggingEvent log) {
    synchronized (messages) {
      messages.get(System.currentTimeMillis()).add(log);
    }
  }

  public static ArrayList<LogMessage> getUpdates(final long since) throws IOException {
    if (INSTANCE == null) {
      throw new IOException("ApplicationAppender not initialized.");
    }

    return INSTANCE.messages.getUpdates(since).stream()
        .map(e -> LogMessage.create().time(e.getTimeStamp()).message(e.getFormattedMessage())).collect(Collectors.toCollection(ArrayList::new));
  }

  public static void shutdown() {
    INSTANCE.cleanExecutor.shutdownNow();
  }
}
