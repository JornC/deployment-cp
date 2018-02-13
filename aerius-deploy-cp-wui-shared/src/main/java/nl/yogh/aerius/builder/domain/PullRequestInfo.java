package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PullRequestInfo implements Serializable, IsSerializable {
  private static final long serialVersionUID = 3354703304031517914L;

  private transient boolean incomplete;
  private transient long lastUpdated;

  private boolean busy;

  private String hash;

  private String idx;
  private String title;
  private String author;
  private String url;

  private HashMap<CompositionType, CompositionInfo> compositions;

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

  public PullRequestInfo idx(final int idx) {
    return idx(String.valueOf(idx));
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

  public String url() {
    return url;
  }

  public PullRequestInfo url(final String url) {
    this.url = url;
    return this;
  }

  public String hash() {
    return hash;
  }

  public PullRequestInfo hash(final String hash) {
    this.hash = hash;
    return this;
  }

  public boolean isBusy() {
    return busy;
  }

  public PullRequestInfo busy(final boolean busy) {
    this.busy = busy;
    return this;
  }

  public boolean isIncomplete() {
    return incomplete;
  }

  public PullRequestInfo incomplete(final boolean incomplete) {
    this.incomplete = incomplete;
    return this;
  }

  public long lastUpdated() {
    return lastUpdated;
  }

  public PullRequestInfo lastUpdated(final long lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  public HashMap<CompositionType, CompositionInfo> compositions() {
    return compositions;
  }

  public PullRequestInfo compositions(final HashMap<CompositionType, CompositionInfo> compositions) {
    this.compositions = compositions;
    return this;
  }
}
