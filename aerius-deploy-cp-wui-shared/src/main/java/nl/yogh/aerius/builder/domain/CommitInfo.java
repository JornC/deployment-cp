package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CommitInfo implements Serializable, IsSerializable {
  private static final long serialVersionUID = 3354703304031517914L;

  private transient boolean incomplete;
  private transient long lastUpdated;

  private boolean master;
  private boolean branch;
  private boolean pull;

  private boolean busy;

  private String hash;

  private String idx;
  private String title;
  private String author;
  private String url;

  private HashMap<CompositionType, CompositionInfo> compositions;

  public static CommitInfo create() {
    return new CommitInfo();
  }

  public boolean isMaster() {
    return master;
  }

  public CommitInfo master(final boolean master) {
    this.master = master;
    return this;
  }

  public boolean isBranch() {
    return branch;
  }

  public CommitInfo branch(final boolean branch) {
    this.branch = branch;
    return this;
  }

  public boolean isPull() {
    return pull;
  }

  public CommitInfo pull(final boolean pull) {
    this.pull = pull;
    return this;
  }

  public String idx() {
    return idx;
  }

  public CommitInfo idx(final String idx) {
    this.idx = idx;
    return this;
  }

  public CommitInfo idx(final int idx) {
    return idx(String.valueOf(idx));
  }

  public String title() {
    return title;
  }

  public CommitInfo title(final String title) {
    this.title = title;
    return this;
  }

  public String author() {
    return author;
  }

  public CommitInfo author(final String author) {
    this.author = author;
    return this;
  }

  public String url() {
    return url;
  }

  public CommitInfo url(final String url) {
    this.url = url;
    return this;
  }

  public String hash() {
    return hash;
  }

  public CommitInfo hash(final String hash) {
    this.hash = hash;
    return this;
  }

  public boolean isBusy() {
    return busy;
  }

  public CommitInfo busy(final boolean busy) {
    this.busy = busy;
    return this;
  }

  public boolean isIncomplete() {
    return incomplete;
  }

  public CommitInfo incomplete(final boolean incomplete) {
    this.incomplete = incomplete;
    return this;
  }

  public long lastUpdated() {
    return lastUpdated;
  }

  public CommitInfo lastUpdated(final long lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  public HashMap<CompositionType, CompositionInfo> compositions() {
    return compositions;
  }

  public CommitInfo compositions(final HashMap<CompositionType, CompositionInfo> compositions) {
    this.compositions = compositions;
    return this;
  }
}
