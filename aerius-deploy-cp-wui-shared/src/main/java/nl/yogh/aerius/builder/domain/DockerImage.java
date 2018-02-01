package nl.yogh.aerius.builder.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DockerImage implements Serializable, IsSerializable, HasHash {
  private static final long serialVersionUID = -1848619391020318009L;

  private String hash;
  private String name;
  private String tag;

  public DockerImage() {}

  public static DockerImage create() {
    return new DockerImage();
  }

  @Override
  public String hash() {
    return hash;
  }

  public DockerImage hash(final String hash) {
    this.hash = hash;
    return this;
  }

  public String name() {
    return name;
  }

  public DockerImage name(final String name) {
    this.name = name;
    return this;
  }

  public String tag() {
    return tag;
  }

  public DockerImage tag(final String tag) {
    this.tag = tag;
    return this;
  }
}
