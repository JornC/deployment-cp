package nl.yogh.aerius.builder.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.domain.ProjectDeploymentAction;
import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.exception.ApplicationException;

@RemoteServiceRelativePath(ServiceURLConstants.PULL_REQUEST_GWT_PATH)
public interface PullRequestService extends RemoteService {
  ArrayList<PullRequestInfo> getPullRequests() throws ApplicationException;

  PresentSnapshot getPresentSituation() throws ApplicationException;

  ArrayList<ProjectInfo> getProductUpdates(long since) throws ApplicationException;

  ArrayList<ServiceInfo> getServiceUpdates(long since) throws ApplicationException;

  ProjectInfo doAction(String idx, ProjectDeploymentAction action, ProjectInfo info) throws ApplicationException;
}
