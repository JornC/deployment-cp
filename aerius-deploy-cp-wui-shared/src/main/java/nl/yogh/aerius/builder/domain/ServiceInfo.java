package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ServiceInfo implements Serializable, IsSerializable, HasHash {
  private static final long serialVersionUID = -7054855770805465459L;

  private ServiceType type;
  private ServiceStatus status;
  private String hash;

  public ServiceInfo() {}

  public static ServiceInfo create() {
    return new ServiceInfo();
  }

  public static ServiceInfo create(final ShallowServiceInfo service) {
    return create().hash(service.hash()).type(service.type());
  }

  public ServiceStatus status() {
    return status;
  }

  public ServiceInfo status(final ServiceStatus status) {
    this.status = status;
    return this;
  }

  @Override
  public String hash() {
    return hash;
  }

  public ServiceInfo hash(final String hash) {
    this.hash = hash;
    return this;
  }

  public ServiceType type() {
    return type;
  }

  public ServiceInfo type(final ServiceType type) {
    this.type = type;
    return this;
  }

  @Override
  public String toString() {
    return "ServiceInfo [status=" + status + ", hash=" + hash + "]";
  }
}
