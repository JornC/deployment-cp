package nl.yogh.aerius.builder.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import nl.yogh.aerius.builder.domain.LogMessage;
import nl.yogh.aerius.builder.exception.ApplicationException;

@RemoteServiceRelativePath(ServiceURLConstants.LOG_GWT_PATH)
public interface LogService extends RemoteService {
  ArrayList<LogMessage> getLogMessages(long since) throws ApplicationException;
}
