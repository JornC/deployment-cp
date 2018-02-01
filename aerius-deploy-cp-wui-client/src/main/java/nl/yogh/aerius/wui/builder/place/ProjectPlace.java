package nl.yogh.aerius.wui.builder.place;

import java.util.Map;

import nl.yogh.gwt.wui.place.ApplicationPlace;

public class ProjectPlace extends ApplicationPlace {
  public static class Tokenizer extends ProjectPlace.AbstractTokenizer<ProjectPlace> {

    @Override
    protected ProjectPlace createPlace() {
      return new ProjectPlace();
    }
  }

  public abstract static class AbstractTokenizer<T extends ProjectPlace> extends ApplicationPlace.Tokenizer<T> {
    @Override
    protected void updatePlace(final Map<String, String> tokens, final T place) {}

    @Override
    protected void setTokenMap(final T place, final Map<String, String> tokens) {}
  }

  public ProjectPlace copy() {
    return copyTo(new ProjectPlace());
  }

  public <E extends ProjectPlace> E copyTo(final E copy) {
    return copy;
  }

  @Override
  public String toString() {
    return "ProjectPlace []";
  }
}
