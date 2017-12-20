package nl.yogh.aerius.wui.builder.place;

import java.util.Map;

import nl.yogh.gwt.wui.place.ApplicationPlace;

public class LogPlace extends ApplicationPlace {
  public static class Tokenizer extends LogPlace.AbstractTokenizer<LogPlace> {

    @Override
    protected LogPlace createPlace() {
      return new LogPlace();
    }
  }

  public abstract static class AbstractTokenizer<T extends LogPlace> extends ApplicationPlace.Tokenizer<T> {
    @Override
    protected void updatePlace(final Map<String, String> tokens, final T place) {
    }

    @Override
    protected void setTokenMap(final T place, final Map<String, String> tokens) {
    }
  }

  public LogPlace copy() {
    return copyTo(new LogPlace());
  }

  public <E extends LogPlace> E copyTo(final E copy) {
    return copy;
  }

  @Override
  public String toString() {
    return "ContainerPlace []";
  }
}
