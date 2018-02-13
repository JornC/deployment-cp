package nl.yogh.aerius.builder.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.domain.CompositionDeploymentAction;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.exception.ApplicationException;

@RemoteServiceRelativePath(ServiceURLConstants.PULL_REQUEST_GWT_PATH)
public interface PullRequestService extends RemoteService {
  ArrayList<PullRequestInfo> getPullRequests() throws ApplicationException;

  PresentSnapshot getPresentSituation() throws ApplicationException;

  ArrayList<CompositionInfo> getProductUpdates(long since) throws ApplicationException;

  ArrayList<ServiceInfo> getServiceUpdates(long since) throws ApplicationException;

  CompositionInfo doAction(String idx, CompositionDeploymentAction action, CompositionInfo info) throws ApplicationException;
}
