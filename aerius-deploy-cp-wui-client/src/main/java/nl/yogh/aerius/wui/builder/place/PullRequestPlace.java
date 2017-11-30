package nl.yogh.aerius.wui.builder.place;

import java.util.Map;

import nl.yogh.gwt.wui.place.ApplicationPlace;

public class PullRequestPlace extends ApplicationPlace {
  public static class Tokenizer extends PullRequestPlace.AbstractTokenizer<PullRequestPlace> {

    @Override
    protected PullRequestPlace createPlace() {
      return new PullRequestPlace();
    }
  }

  public abstract static class AbstractTokenizer<T extends PullRequestPlace> extends ApplicationPlace.Tokenizer<T> {
    @Override
    protected void updatePlace(final Map<String, String> tokens, final T place) {
    }

    @Override
    protected void setTokenMap(final T place, final Map<String, String> tokens) {
    }
  }

  public PullRequestPlace copy() {
    return copyTo(new PullRequestPlace());
  }

  public <E extends PullRequestPlace> E copyTo(final E copy) {
    return copy;
  }

  @Override
  public String toString() {
    return "PullRequestPlace []";
  }
}
