package nl.yogh.aerius.server.servlet;

import java.util.logging.LogRecord;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.logging.shared.RemoteLoggingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@WebServlet("/application/remote_logging")
public class RemoteLoggingServlet extends RemoteServiceServlet implements RemoteLoggingService {
  private static final long serialVersionUID = -7410336912430084167L;

  private static final Logger LOG = LoggerFactory.getLogger(RemoteLoggingServlet.class);

  @Override
  public String logOnServer(final LogRecord record) {
    // The default logger reports to stderr for some insane reason.
    LOG.info(record.getMessage());

    return null;
  }
}
