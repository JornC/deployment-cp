package nl.yogh.aerius.builder.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import nl.yogh.aerius.builder.domain.CompositionType;
import nl.yogh.aerius.builder.exception.ApplicationException;

@RemoteServiceRelativePath(ServiceURLConstants.APPLICATION_REQUEST_GWT_PATH)
public interface ApplicationService extends RemoteService {
  ArrayList<CompositionType> getApplicationConfig() throws ApplicationException;
}
