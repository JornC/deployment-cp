package nl.yogh.aerius.server;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.service.PullRequestService;
import nl.yogh.aerius.builder.service.ServiceURLConstants;

@WebServlet(ServiceURLConstants.MODULE_NAME + ServiceURLConstants.PULL_REQUEST_GWT_PATH)
public class PullRequestServiceImpl extends RemoteServiceServlet implements PullRequestService {
  private static final long serialVersionUID = 8422984952374441883L;

  @Override
  public ArrayList<PullRequestInfo> getPullRequests() {
    final ArrayList<PullRequestInfo> lst = new ArrayList<>();

    lst.add(new PullRequestInfo(654));
    lst.add(new PullRequestInfo(123));
    lst.add(new PullRequestInfo(312));
    lst.add(new PullRequestInfo(589));

    return lst;
  }
}
