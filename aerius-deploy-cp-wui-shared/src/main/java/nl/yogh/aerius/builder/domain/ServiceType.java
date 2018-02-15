package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ServiceType implements Serializable, IsSerializable {
  private static final long serialVersionUID = 7891400062587193824L;

  private String name;

  public ServiceType() {}

  public ServiceType(final String name) {
    this.name = name.intern();
  }

  public String name() {
    return name;
  }

  public ServiceType name(final String name) {
    this.name = name;
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ServiceType other = (ServiceType) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return name();
  }
}
