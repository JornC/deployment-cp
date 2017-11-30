package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PullRequestInfo implements Serializable, IsSerializable {
  private static final long serialVersionUID = 3354703304031517914L;

  private int pullRequestIdx;
  private String title;
  private String authorName;

  public PullRequestInfo() {}

  public PullRequestInfo(final int pullRequestIdx) {
    this.pullRequestIdx = pullRequestIdx;
  }

  public int getPullRequestIdx() {
    return pullRequestIdx;
  }

  public void setPullRequestIdx(final int pullRequestIdx) {
    this.pullRequestIdx = pullRequestIdx;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }
}
