package nl.yogh.aerius.wui.builder.history;

import com.google.gwt.core.client.GWT;

import nl.yogh.aerius.wui.builder.place.DockerPlace;
import nl.yogh.aerius.wui.builder.place.LogPlace;
import nl.yogh.aerius.wui.builder.place.ProjectPlace;
import nl.yogh.aerius.wui.builder.place.PullRequestPlace;
import nl.yogh.gwt.wui.history.PlaceHistoryMapper;
import nl.yogh.gwt.wui.place.ApplicationPlace;

public class BuilderPlaceHistoryMapper implements PlaceHistoryMapper {
  private static final String PROJECTS = "projects";
  private static final String DOCKER = "docker";
  private static final String PULL_REQUESTS = "pulls";
  private static final String LOGS = "logs";

  private static final String SEPERATOR = ":";

  @Override
  public String getToken(final ApplicationPlace value) {
    String token = "";

    if (value instanceof ProjectPlace) {
      token = PROJECTS + SEPERATOR + new ProjectPlace.Tokenizer().getToken((ProjectPlace) value);
    } else if (value instanceof PullRequestPlace) {
      token = PULL_REQUESTS + SEPERATOR + new PullRequestPlace.Tokenizer().getToken((PullRequestPlace) value);
    } else if (value instanceof LogPlace) {
      token = LOGS + SEPERATOR + new LogPlace.Tokenizer().getToken((LogPlace) value);
    } else if (value instanceof DockerPlace) {
      token = DOCKER + SEPERATOR + new DockerPlace.Tokenizer().getToken((DockerPlace) value);
    }

    return token;
  }

  @Override
  public ApplicationPlace getPlace(final String token) {
    final String[] tokens = token.split(SEPERATOR, 2);

    ApplicationPlace place;

    GWT.log("Token split: " + tokens[0] + " >> " + tokens[1]);

    switch (tokens[0]) {
    case PROJECTS:
      place = new ProjectPlace.Tokenizer().getPlace(tokens[1]);
      break;
    case PULL_REQUESTS:
      place = new PullRequestPlace.Tokenizer().getPlace(tokens[1]);
      break;
    case LOGS:
      place = new LogPlace.Tokenizer().getPlace(tokens[1]);
      break;
    case DOCKER:
      place = new DockerPlace.Tokenizer().getPlace(tokens[1]);
      break;
    default:
      place = null;
    }

    return place;
  }
}
