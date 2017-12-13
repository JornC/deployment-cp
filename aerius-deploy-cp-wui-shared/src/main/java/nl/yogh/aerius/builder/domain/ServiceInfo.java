package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ServiceInfo implements Serializable, IsSerializable {
  private static final long serialVersionUID = -8851590477783803916L;

  private ServiceStatus status;
  private String hash;

  public ServiceInfo() {}

  public static ServiceInfo create() {
    return new ServiceInfo();
  }

  public ServiceStatus status() {
    return status;
  }

  public ServiceInfo status(final ServiceStatus status) {
    this.status = status;
    return this;
  }

  public String hash() {
    return hash;
  }

  public ServiceInfo hash(final String hash) {
    this.hash = hash;
    return this;
  }

  @Override
  public String toString() {
    return "ServiceInfo [status=" + status + ", hash=" + hash + "]";
  }
}
