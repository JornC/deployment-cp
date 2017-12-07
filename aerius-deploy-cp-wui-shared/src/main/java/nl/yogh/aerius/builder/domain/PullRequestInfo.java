package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PullRequestInfo implements Serializable, IsSerializable {
  private static final long serialVersionUID = 3354703304031517914L;

  private String idx;
  private String title;
  private String author;

  public static PullRequestInfo create() {
    return new PullRequestInfo();
  }

  public String idx() {
    return idx;
  }

  public PullRequestInfo idx(final String idx) {
    this.idx = idx;
    return this;
  }

  public String title() {
    return title;
  }

  public PullRequestInfo title(final String title) {
    this.title = title;
    return this;
  }

  public String author() {
    return author;
  }

  public PullRequestInfo author(final String author) {
    this.author = author;
    return this;
  }
}
