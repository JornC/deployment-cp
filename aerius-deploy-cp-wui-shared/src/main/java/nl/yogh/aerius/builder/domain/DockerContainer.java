package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DockerContainer implements Serializable, IsSerializable, HasHash {
  private static final long serialVersionUID = -1848619391020318009L;

  private String hash;
  private String name;
  private String image;

  public DockerContainer() {}

  public static DockerContainer create() {
    return new DockerContainer();
  }

  @Override
  public String hash() {
    return hash;
  }

  public DockerContainer hash(final String hash) {
    this.hash = hash;
    return this;
  }

  public String image() {
    return image;
  }

  public DockerContainer image(final String image) {
    this.image = image;
    return this;
  }

  public String name() {
    return name;
  }

  public DockerContainer name(final String name) {
    this.name = name;
    return this;
  }
}
