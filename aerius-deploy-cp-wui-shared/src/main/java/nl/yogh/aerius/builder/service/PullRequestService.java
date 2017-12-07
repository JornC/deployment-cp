package nl.yogh.aerius.builder.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;

@RemoteServiceRelativePath(ServiceURLConstants.PULL_REQUEST_GWT_PATH)
public interface PullRequestService extends RemoteService {
  ArrayList<PullRequestInfo> getPullRequests();

  ProductInfo doAction(ProductDeploymentAction action, ProductType type, ProductInfo info);
}
