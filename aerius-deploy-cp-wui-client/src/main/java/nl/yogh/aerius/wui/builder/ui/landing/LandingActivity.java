package nl.yogh.aerius.wui.builder.ui.landing;

import javax.inject.Inject;

import nl.yogh.aerius.wui.builder.ui.landing.LandingView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;

public class LandingActivity extends EventActivity<Presenter, LandingView> implements Presenter {

  @Inject
  public LandingActivity(final LandingView view) {
    super(view);
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
