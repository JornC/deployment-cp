package nl.yogh.aerius.wui.builder.place;

import java.util.Map;

import nl.yogh.gwt.wui.place.ApplicationPlace;

public class DockerPlace extends ApplicationPlace {
  public static class Tokenizer extends DockerPlace.AbstractTokenizer<DockerPlace> {

    @Override
    protected DockerPlace createPlace() {
      return new DockerPlace();
    }
  }

  public abstract static class AbstractTokenizer<T extends DockerPlace> extends ApplicationPlace.Tokenizer<T> {
    @Override
    protected void updatePlace(final Map<String, String> tokens, final T place) {
    }

    @Override
    protected void setTokenMap(final T place, final Map<String, String> tokens) {
    }
  }

  public DockerPlace copy() {
    return copyTo(new DockerPlace());
  }

  public <E extends DockerPlace> E copyTo(final E copy) {
    return copy;
  }

  @Override
  public String toString() {
    return "DockerPlace []";
  }
}
