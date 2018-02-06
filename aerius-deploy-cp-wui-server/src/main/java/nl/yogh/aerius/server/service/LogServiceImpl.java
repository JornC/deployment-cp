package nl.yogh.aerius.server.service;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.LogMessage;
import nl.yogh.aerius.builder.exception.ApplicationException;
import nl.yogh.aerius.builder.exception.ApplicationException.Reason;
import nl.yogh.aerius.builder.service.LogService;
import nl.yogh.aerius.server.logging.ApplicationAppender;

public class LogServiceImpl implements LogService {
  private static final Logger LOG = LoggerFactory.getLogger(LogServiceImpl.class);

  @Override
  public ArrayList<LogMessage> getLogMessages(final long since) throws ApplicationException {
    try {
      return ApplicationAppender.getUpdates(since);
    } catch (final IOException e) {
      LOG.error("IOException while retrieving ApplicationAppender.", e);
      throw new ApplicationException(Reason.INTERNAL_ERROR);
    }
  }
}
