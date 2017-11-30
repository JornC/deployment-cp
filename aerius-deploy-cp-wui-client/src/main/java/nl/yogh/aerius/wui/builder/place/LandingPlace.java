package nl.yogh.aerius.wui.builder.place;

import java.util.Map;

import nl.yogh.gwt.wui.place.ApplicationPlace;

public class LandingPlace extends ApplicationPlace {
  public static class Tokenizer extends LandingPlace.AbstractTokenizer<LandingPlace> {

    @Override
    protected LandingPlace createPlace() {
      return new LandingPlace();
    }
  }

  public abstract static class AbstractTokenizer<T extends LandingPlace> extends ApplicationPlace.Tokenizer<T> {
    @Override
    protected void updatePlace(final Map<String, String> tokens, final T place) {
    }

    @Override
    protected void setTokenMap(final T place, final Map<String, String> tokens) {
    }
  }

  public LandingPlace copy() {
    return copyTo(new LandingPlace());
  }

  public <E extends LandingPlace> E copyTo(final E copy) {
    return copy;
  }

  @Override
  public String toString() {
    return "LandingPlace []";
  }
}
