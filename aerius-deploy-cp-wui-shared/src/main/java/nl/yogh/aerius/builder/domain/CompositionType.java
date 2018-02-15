package nl.yogh.aerius.builder.domain;

import java.io.Serializable;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CompositionType implements Serializable, IsSerializable {
  private static final long serialVersionUID = -3825451067350764904L;

  private HashSet<ServiceType> serviceTypes;
  private String name;

  public CompositionType() {}

  public CompositionType(final String name, final HashSet<ServiceType> serviceTypes) {
    this.name = name;
    this.serviceTypes = serviceTypes;
  }

  public String name() {
    return name;
  }

  public CompositionType name(final String name) {
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
    final CompositionType other = (CompositionType) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  public HashSet<ServiceType> serviceTypes() {
    return serviceTypes;
  }

  public CompositionType serviceTypes(final HashSet<ServiceType> serviceTypes) {
    this.serviceTypes = serviceTypes;
    return this;
  }

  @Override
  public String toString() {
    return name();
  }
}
