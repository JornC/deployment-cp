package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProjectInfo implements Serializable, IsSerializable, HasHash {
  private static final long serialVersionUID = -4108120027600006619L;

  private boolean busy;

  private ServiceStatus status;
  private String hash;

  private ArrayList<String> services;

  public ProjectInfo() {}

  public static ProjectInfo create() {
    return new ProjectInfo();
  }

  public ServiceStatus status() {
    return status;
  }

  public ProjectInfo status(final ServiceStatus status) {
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

  public ArrayList<String> services() {
    return services;
  }

  public ProjectInfo services(final ArrayList<String> services) {
    this.services = services;
    return this;
  }

  @Override
  public String toString() {
    return "ProductInfo [busy=" + busy + ", status=" + status + ", hash=" + hash + "]";
  }
}
