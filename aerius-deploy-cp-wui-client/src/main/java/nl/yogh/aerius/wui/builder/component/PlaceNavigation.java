package nl.yogh.aerius.wui.builder.component;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import nl.yogh.gwt.wui.place.ApplicationPlace;
import nl.yogh.gwt.wui.place.PlaceController;

public abstract class PlaceNavigation<T> extends Composite {
  private static final PlaceNavigationUiBinder UI_BINDER = GWT.create(PlaceNavigationUiBinder.class);

  interface PlaceNavigationUiBinder extends UiBinder<Widget, PlaceNavigation<?>> {}

  public interface CustomStyle extends CssResource {
    String button();
  }

  @UiField CustomStyle style;

  @UiField FlowPanel panel;

  private final PlaceController placeController;

  @Inject
  public PlaceNavigation(final PlaceController placeController) {
    this.placeController = placeController;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  protected abstract Collection<T> getPlaces();

  protected abstract String getBackgroundColor(T thing);

  protected abstract String getFaceColor(T thing);

  protected abstract String getText(T thing);

  protected abstract ApplicationPlace getPlace(T thing);

  protected void buildButtons() {
    for (final T thing : getPlaces()) {
      final Button b = new Button();
      b.setText(getText(thing));
      b.getElement().getStyle().setBackgroundColor(getBackgroundColor(thing));
      b.getElement().getStyle().setColor(getFaceColor(thing));
      b.addStyleName(style.button());
      b.addClickHandler(c -> placeController.goTo(getPlace(thing)));

      panel.add(b);
    }
  }
}
