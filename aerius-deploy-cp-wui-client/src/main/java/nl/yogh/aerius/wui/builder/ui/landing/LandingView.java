package nl.yogh.aerius.wui.builder.ui.landing;

import com.google.inject.ImplementedBy;

import nl.yogh.aerius.wui.builder.ui.landing.LandingView.Presenter;
import nl.yogh.gwt.wui.activity.View;

@ImplementedBy(LandingViewImpl.class)
public interface LandingView extends View<Presenter> {
  public interface Presenter {

  }

}
