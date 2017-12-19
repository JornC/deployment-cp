package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PresentSnapshot implements Serializable, IsSerializable {
  private static final long serialVersionUID = -1057490863139360212L;

  private long lastUpdate;

  private ArrayList<ProjectInfo> products;
  private ArrayList<ServiceInfo> services;

  public PresentSnapshot() {}

  public ArrayList<ProjectInfo> getProducts() {
    return products;
  }

  public void setProducts(final ArrayList<ProjectInfo> products) {
    this.products = products;
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
