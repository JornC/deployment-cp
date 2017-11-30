package nl.yogh.aerius.wui.builder.history;

import com.google.gwt.core.client.GWT;

import nl.yogh.aerius.wui.builder.place.ContainerPlace;
import nl.yogh.aerius.wui.builder.place.PullRequestPlace;
import nl.yogh.gwt.wui.history.PlaceHistoryMapper;
import nl.yogh.gwt.wui.place.ApplicationPlace;

public class BuilderPlaceHistoryMapper implements PlaceHistoryMapper {
  private static final String CONTAINERS = "containers";
  private static final String PULL_REQUESTS = "pulls";

  private static final String SEPERATOR = ":";

  @Override
  public String getToken(final ApplicationPlace value) {
    String token = "";

    if (value instanceof ContainerPlace) {
      token = CONTAINERS + SEPERATOR + new ContainerPlace.Tokenizer().getToken((ContainerPlace) value);
    } else if (value instanceof PullRequestPlace) {
      token = PULL_REQUESTS + SEPERATOR + new PullRequestPlace.Tokenizer().getToken((PullRequestPlace) value);
    }

    return token;
  }

  @Override
  public ApplicationPlace getPlace(final String token) {
    final String[] tokens = token.split(SEPERATOR, 2);

    ApplicationPlace place;

    GWT.log("Token split: " + tokens[0] + " >> " + tokens[1]);

    switch (tokens[0]) {
    case CONTAINERS:
      place = new ContainerPlace.Tokenizer().getPlace(tokens[1]);
      break;
    case PULL_REQUESTS:
      place = new PullRequestPlace.Tokenizer().getPlace(tokens[1]);
      break;
    default:
      place = null;
    }

    return place;
  }
}
