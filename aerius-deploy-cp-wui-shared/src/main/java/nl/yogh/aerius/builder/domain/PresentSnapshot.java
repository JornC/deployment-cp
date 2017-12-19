package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PresentSnapshot implements Serializable, IsSerializable {
  private static final long serialVersionUID = -1057490863139360212L;

  private ArrayList<ProductInfo> products;

  private ArrayList<ServiceInfo> services;

  public PresentSnapshot() {}

  public ArrayList<ProductInfo> getProducts() {
    return products;
  }

  public void setProducts(final ArrayList<ProductInfo> products) {
    this.products = products;
  }

  public ArrayList<ServiceInfo> getServices() {
    return services;
  }

  public void setServices(final ArrayList<ServiceInfo> services) {
    this.services = services;
  }
}
