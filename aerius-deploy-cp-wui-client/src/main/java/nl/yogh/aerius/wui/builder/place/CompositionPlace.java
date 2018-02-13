package nl.yogh.aerius.wui.builder.place;

import java.util.Map;

import nl.yogh.gwt.wui.place.ApplicationPlace;

public class CompositionPlace extends ApplicationPlace {
  public static class Tokenizer extends CompositionPlace.AbstractTokenizer<CompositionPlace> {

    @Override
    protected CompositionPlace createPlace() {
      return new CompositionPlace();
    }
  }

  public abstract static class AbstractTokenizer<T extends CompositionPlace> extends ApplicationPlace.Tokenizer<T> {
    @Override
    protected void updatePlace(final Map<String, String> tokens, final T place) {}

    @Override
    protected void setTokenMap(final T place, final Map<String, String> tokens) {}
  }

  public CompositionPlace copy() {
    return copyTo(new CompositionPlace());
  }

  public <E extends CompositionPlace> E copyTo(final E copy) {
    return copy;
  }

  @Override
  public String toString() {
    return "CompositionPlace []";
  }
}
