package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PresentSnapshot implements Serializable, IsSerializable {
  private static final long serialVersionUID = -1057490863139360212L;

  private long lastUpdate;

  private ArrayList<CompositionInfo> projects;
  private ArrayList<ServiceInfo> services;

  public PresentSnapshot() {}

  public ArrayList<CompositionInfo> getProjects() {
    return projects;
  }

  public void setProjects(final ArrayList<CompositionInfo> projects) {
    this.projects = projects;
  }

  public ArrayList<ServiceInfo> getServices() {
    return services;
  }

  public void setServices(final ArrayList<ServiceInfo> services) {
    this.services = services;
  }

  public long getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(long lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}
