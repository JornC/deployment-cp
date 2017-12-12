package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.PullRequestInfo;

public class AERIUSGithubHook {
  private static final Logger LOG = LoggerFactory.getLogger(AERIUSGithubHook.class);

  private final GitHubClient client;

  public AERIUSGithubHook(final String oAuthToken) {
    client = new GitHubClient();
    client.setOAuth2Token(oAuthToken);
  }

  public void update(final Map<Integer, PullRequestInfo> pulls) throws IOException {
    final RepositoryService repoService = new RepositoryService(client);
    final Repository repository = repoService.getRepository("aerius", "AERIUS-II");

    final PullRequestService pullService = new PullRequestService(client);
    final List<PullRequest> pullRequests = pullService.getPullRequests(repository, "open");

    for (final PullRequest pull : pullRequests) {
      final int idx = pull.getNumber();

      final PullRequestInfo info = PullRequestInfo.create();
      info.author(pull.getUser().getLogin());
      info.idx(idx);
      info.title(pull.getTitle());
      info.url(pull.getHtmlUrl());
      info.hash(pull.getHead().getSha());

      checkUpdate(pulls, idx, info);
    }
  }

  public void checkUpdate(final Map<Integer, PullRequestInfo> pulls, final int idx, final PullRequestInfo info) {
    if (pulls.containsKey(idx)) {
      final PullRequestInfo original = pulls.get(idx);
      if (original.hash().equals(info.hash())) {
        return;
      }
    }

    update(pulls, idx, info);
  }

  private void update(final Map<Integer, PullRequestInfo> pulls, final int idx, final PullRequestInfo info) {
    info.incomplete(true);
    pulls.put(idx, info);
  }
}
