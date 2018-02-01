package nl.yogh.aerius.wui.builder.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import nl.yogh.aerius.wui.builder.place.DockerPlace;
import nl.yogh.aerius.wui.builder.place.LogPlace;
import nl.yogh.aerius.wui.builder.place.ProjectPlace;
import nl.yogh.aerius.wui.builder.place.PullRequestPlace;
import nl.yogh.aerius.wui.i18n.M;
import nl.yogh.aerius.wui.resources.R;
import nl.yogh.gwt.wui.place.ApplicationPlace;
import nl.yogh.gwt.wui.place.PlaceController;

public class BuilderPlaceNavigation extends PlaceNavigation<String> {
  private final Map<String, ApplicationPlace> places = new HashMap<>();

  @Inject
  public BuilderPlaceNavigation(final PlaceController placeController) {
    super(placeController);

    places.put(PullRequestPlace.class.getSimpleName(), new PullRequestPlace());
    places.put(ProjectPlace.class.getSimpleName(), new ProjectPlace());
    places.put(DockerPlace.class.getSimpleName(), new DockerPlace());
    places.put(LogPlace.class.getSimpleName(), new LogPlace());

    buildButtons();
  }

  @Override
  protected Collection<String> getPlaces() {
    return places.keySet();
  }

  @Override
  protected String getBackgroundColor(final String thing) {
    return R.colors().placeBackgroundColor(thing);
  }

  @Override
  protected String getFaceColor(final String thing) {
    return R.colors().placeFontColor(thing);
  }

  @Override
  protected String getText(final String thing) {
    return M.messages().placeButtonText(thing);
  }

  @Override
  protected ApplicationPlace getPlace(final String thing) {
    return places.get(thing);
  }
}
