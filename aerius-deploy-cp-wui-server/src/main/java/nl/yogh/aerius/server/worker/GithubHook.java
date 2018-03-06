package nl.yogh.aerius.server.worker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.CommitInfo;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.server.util.ApplicationConfiguration;

public class GithubHook {
  private static final Logger LOG = LoggerFactory.getLogger(GithubHook.class);

  private final GitHubClient client;

  private final ApplicationConfiguration cfg;

  public GithubHook(final ApplicationConfiguration cfg) {
    this.cfg = cfg;
    client = new GitHubClient();
    client.setOAuth2Token(cfg.getGithubOpenAuthToken());
  }

  public void update(final Map<String, CompositionInfo> projects, final Map<String, CommitInfo> pulls) throws IOException {
    LOG.debug("Retrieving pull requests from GitHub.");

    final RepositoryService repoService = new RepositoryService(client);
    final Repository repository = repoService.getRepository(cfg.getGithubUserName(), cfg.getGithubRepositoryName());

    final PullRequestService pullService = new PullRequestService(client);
    final List<PullRequest> pullRequests = pullService.getPullRequests(repository, "open");

    int updated = 0;
    for (final PullRequest pull : pullRequests) {
      final String idx = String.valueOf(pull.getNumber());

      final CommitInfo info = CommitInfo.create();
      info.pull(true);
      info.author(pull.getUser().getLogin());
      info.idx(idx);
      info.title(pull.getTitle());
      info.url(pull.getHtmlUrl());
      info.hash(pull.getHead().getSha());

      if (checkUpdate(projects, pulls, idx, info)) {
        updated++;
      }
    }

    for (final RepositoryBranch branch : repoService.getBranches(repository)) {
      final CommitInfo info = CommitInfo.create();
      info.author("N/A");
      info.idx(branch.getName());
      LOG.info("Parsing branch: {}", branch.getName());
      if (branch.getName().equals(repository.getMasterBranch())) {
        info.master(true);
        info.branch(false);
      } else {
        info.branch(true);
      }
      info.title(branch.getName());
      info.url(branch.getCommit().getSha());
      info.hash(branch.getCommit().getSha());

      if (checkUpdate(projects, pulls, branch.getCommit().getSha(), info)) {
        updated++;
      }
    }

    if (updated > 0) {
      LOG.info("Updated {} pull requests.", updated);
    }
  }

  public boolean checkUpdate(final Map<String, CompositionInfo> projects, final Map<String, CommitInfo> pulls, final String idx,
      final CommitInfo info) {
    if (pulls.containsKey(idx)) {
      final CommitInfo original = pulls.get(idx);
      if (original.hash().equals(info.hash())) {
        return false;
      }
    }

    update(projects, pulls, idx, info);
    return true;
  }

  private void update(final Map<String, CompositionInfo> projects, final Map<String, CommitInfo> pulls, final String idx,
      final CommitInfo info) {
    info.incomplete(true);
    projects.remove(info.hash());

    synchronized (pulls) {
      pulls.put(idx, info);
    }
  }
}
