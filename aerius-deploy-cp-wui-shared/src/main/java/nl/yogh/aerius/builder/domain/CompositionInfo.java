package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CompositionInfo implements Serializable, IsSerializable, HasHash {
  private static final long serialVersionUID = -4108120027600006619L;

  private boolean busy;

  private CompositionType type;
  private CompositionStatus status;
  private String hash;

  private String buildHash;

  private String url;

  private ArrayList<ServiceInfo> services;

  private CommitInfo commit;

  public CompositionInfo() {}

  public static CompositionInfo create() {
    return new CompositionInfo();
  }

  public CompositionStatus status() {
    return status;
  }

  public CompositionInfo status(final CompositionStatus status) {
    this.status = status;
    return this;
  }

  @Override
  public String hash() {
    return hash;
  }

  public CompositionInfo hash(final String hash) {
    this.hash = hash;
    return this;
  }

  public String buildHash() {
    return buildHash;
  }

  public CompositionInfo buildHash(final String buildHash) {
    this.buildHash = buildHash;
    return this;
  }

  public boolean busy() {
    return busy;
  }

  public CompositionInfo busy(final boolean busy) {
    this.busy = busy;
    return this;
  }

  public CompositionType type() {
    return type;
  }

  public CompositionInfo type(final CompositionType type) {
    this.type = type;
    return this;
  }

  public String url() {
    return url;
  }

  public CompositionInfo url(final String url) {
    this.url = url;
    return this;
  }

  public CommitInfo commit() {
    return commit;
  }

  public CompositionInfo commit(final CommitInfo commit) {
    this.commit = commit;
    return this;
  }

  public ArrayList<ServiceInfo> services() {
    return services;
  }

  public CompositionInfo services(final ArrayList<ServiceInfo> services) {
    this.services = services;
    return this;
  }

  @Override
  public String toString() {
    return "ProductInfo [busy=" + busy + ", status=" + status + ", hash=" + hash + ", services=" + services + "]";
  }
}
