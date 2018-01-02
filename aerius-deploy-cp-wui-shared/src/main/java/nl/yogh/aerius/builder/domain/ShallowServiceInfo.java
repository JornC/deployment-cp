package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ShallowServiceInfo implements Serializable, IsSerializable, HasHash {
  private static final long serialVersionUID = -1257314591946076648L;

  private String hash;
  private ServiceType type;

  public ShallowServiceInfo() {}

  public static ShallowServiceInfo create() {
    return new ShallowServiceInfo();
  }

  @Override
  public String hash() {
    return hash;
  }

  public ShallowServiceInfo hash(final String hash) {
    this.hash = hash;
    return this;
  }

  @Override
  public String toString() {
    return "ShallowServiceInfo [hash=" + hash + "]";
  }

  public ServiceType type() {
    return type;
  }

  public ShallowServiceInfo type(final ServiceType type) {
    this.type = type;
    return this;
  }
}
