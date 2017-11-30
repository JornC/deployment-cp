package nl.yogh.aerius.wui.builder.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import nl.yogh.aerius.wui.builder.place.ContainerPlace;
import nl.yogh.aerius.wui.builder.place.LandingPlace;
import nl.yogh.aerius.wui.builder.place.PullRequestPlace;
import nl.yogh.aerius.wui.i18n.M;
import nl.yogh.aerius.wui.util.ColorUtil;
import nl.yogh.gwt.wui.place.ApplicationPlace;
import nl.yogh.gwt.wui.place.PlaceController;

public class BuilderPlaceNavigation extends PlaceNavigation<Class<?>> {
  private final Map<Class<?>, ApplicationPlace> places = new HashMap<>();

  @Inject
  public BuilderPlaceNavigation(final PlaceController placeController) {
    super(placeController);

    places.put(LandingPlace.class, new LandingPlace());
    places.put(PullRequestPlace.class, new PullRequestPlace());
    places.put(ContainerPlace.class, new ContainerPlace());

    buildButtons();
  }

  @Override
  protected Collection<Class<?>> getPlaces() {
    return places.keySet();
  }

  @Override
  protected String getBackgroundColor(final Class<?> thing) {
    return ColorUtil.getPlaceBackgroundColor(thing);
  }

  @Override
  protected String getFaceColor(final Class<?> thing) {
    return ColorUtil.getPlaceFontColor(thing);
  }

  @Override
  protected String getText(final Class<?> thing) {
    return M.messages().placeButtonText(thing.getSimpleName());
  }

  @Override
  protected ApplicationPlace getPlace(final Class<?> thing) {
    return places.get(thing);
  }
}
