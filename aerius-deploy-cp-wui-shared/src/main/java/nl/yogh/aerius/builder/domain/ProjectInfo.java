package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProjectInfo implements Serializable, IsSerializable, HasHash {
  private static final long serialVersionUID = -4108120027600006619L;

  private boolean busy;

  private ProjectType type;
  private ProjectStatus status;
  private String hash;

  private ArrayList<ShallowServiceInfo> services;

  public ProjectInfo() {}

  public static ProjectInfo create() {
    return new ProjectInfo();
  }

  public ProjectStatus status() {
    return status;
  }

  public ProjectInfo status(final ProjectStatus status) {
    this.status = status;
    return this;
  }

  @Override
  public String hash() {
    return hash;
  }

  public ProjectInfo hash(final String hash) {
    this.hash = hash;
    return this;
  }

  public boolean busy() {
    return busy;
  }

  public ProjectInfo busy(final boolean busy) {
    this.busy = busy;
    return this;
  }

  public ProjectType type() {
    return type;
  }

  public ProjectInfo type(final ProjectType type) {
    this.type = type;
    return this;
  }

  public ArrayList<ShallowServiceInfo> services() {
    return services;
  }

  public ProjectInfo services(final ArrayList<ShallowServiceInfo> services) {
    this.services = services;
    return this;
  }

  @Override
  public String toString() {
    return "ProductInfo [busy=" + busy + ", status=" + status + ", hash=" + hash + ", services=" + services + "]";
  }
}
