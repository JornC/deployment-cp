package nl.yogh.aerius.wui.builder.place;

import java.util.Map;

import nl.yogh.gwt.wui.place.ApplicationPlace;

public class ContainerPlace extends ApplicationPlace {
  public static class Tokenizer extends ContainerPlace.AbstractTokenizer<ContainerPlace> {

    @Override
    protected ContainerPlace createPlace() {
      return new ContainerPlace();
    }
  }

  public abstract static class AbstractTokenizer<T extends ContainerPlace> extends ApplicationPlace.Tokenizer<T> {
    @Override
    protected void updatePlace(final Map<String, String> tokens, final T place) {
    }

    @Override
    protected void setTokenMap(final T place, final Map<String, String> tokens) {
    }
  }

  public ContainerPlace copy() {
    return copyTo(new ContainerPlace());
  }

  public <E extends ContainerPlace> E copyTo(final E copy) {
    return copy;
  }

  @Override
  public String toString() {
    return "ContainerPlace []";
  }
}
