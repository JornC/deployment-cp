package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProductInfo implements Serializable, IsSerializable {
  private static final long serialVersionUID = -4108120027600006619L;

  private boolean busy;

  private ServiceStatus status;
  private String hash;

  public static ProductInfo create() {
    return new ProductInfo();
  }

  public ServiceStatus status() {
    return status;
  }

  public ProductInfo status(final ServiceStatus status) {
    this.status = status;
    return this;
  }

  public String hash() {
    return hash;
  }

  public ProductInfo hash(final String hash) {
    this.hash = hash;
    return this;
  }

  public boolean busy() {
    return busy;
  }

  public ProductInfo busy(final boolean busy) {
    this.busy = busy;
    return this;
  }

  @Override
  public String toString() {
    return "ProductInfo [busy=" + busy + ", status=" + status + ", hash=" + hash + "]";
  }
}
