package nl.yogh.aerius.wui.builder;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;

import nl.yogh.aerius.wui.builder.place.CompositionPlace;
import nl.yogh.aerius.wui.builder.place.DockerPlace;
import nl.yogh.aerius.wui.builder.place.LogPlace;
import nl.yogh.aerius.wui.builder.place.PullRequestPlace;
import nl.yogh.gwt.wui.activity.Activity;
import nl.yogh.gwt.wui.activity.ActivityMapper;
import nl.yogh.gwt.wui.place.ApplicationPlace;
import nl.yogh.gwt.wui.place.DefaultPlace;
import nl.yogh.gwt.wui.place.Place;

public class BuilderActivityMapper implements ActivityMapper {
  private final BuilderActivityFactory factory;

  @Inject
  public BuilderActivityMapper(@DefaultPlace final ApplicationPlace place, final BuilderActivityFactory factory) {
    this.factory = factory;
  }

  @Override
  public Activity<?> getActivity(final Place place) {
    Activity<?> presenter = null;

    presenter = tryGetActivity(place);

    if (presenter == null) {
      GWT.log("Presenter is null: Place ends up nowhere. " + place);
      throw new RuntimeException("No Presenter found for place " + place);
    }

    return presenter;
  }

  private Activity<?> tryGetActivity(final Place place) {
    Activity<?> presenter = null;

    if (place instanceof CompositionPlace) {
      presenter = factory.createCompositionPresenter((CompositionPlace) place);
    } else if (place instanceof DockerPlace) {
      presenter = factory.createDockerPresenter((DockerPlace) place);
    } else if (place instanceof PullRequestPlace) {
      presenter = factory.createPullRequestPresenter((PullRequestPlace) place);
    } else if (place instanceof LogPlace) {
      presenter = factory.createLogPresenter((LogPlace) place);
    }

    return presenter;
  }
}
