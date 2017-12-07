package nl.yogh.aerius.server.service;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.service.PullRequestService;

public class PullRequestServiceImpl implements PullRequestService {
  @Override
  public ArrayList<PullRequestInfo> getPullRequests() {
    final ArrayList<PullRequestInfo> lst = new ArrayList<>();

    lst.add(PullRequestInfo.create().title("OL4 refactor").author("JornC").idx("654"));
    lst.add(PullRequestInfo.create().title("RWS Item road base layer").author("JohnV").idx("123"));
    lst.add(PullRequestInfo.create().title("Only lookup source if we have imear").author("JohnV").idx("312"));
    lst.add(PullRequestInfo.create().title("User geo layer batch inserter").author("JacobusXIII").idx("589"));

    return lst;
  }
}
