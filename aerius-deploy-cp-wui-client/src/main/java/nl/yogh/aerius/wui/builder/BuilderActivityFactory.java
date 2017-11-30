package nl.yogh.aerius.wui.builder;

import nl.yogh.aerius.wui.builder.place.ContainerPlace;
import nl.yogh.aerius.wui.builder.place.LandingPlace;
import nl.yogh.aerius.wui.builder.place.PullRequestPlace;
import nl.yogh.aerius.wui.builder.ui.container.ContainerActivity;
import nl.yogh.aerius.wui.builder.ui.landing.LandingActivity;
import nl.yogh.aerius.wui.builder.ui.pulls.PullRequestActivity;

public interface BuilderActivityFactory {
  ContainerActivity createContainerPresenter(ContainerPlace place);

  LandingActivity createLandingPresenter(LandingPlace place);

  PullRequestActivity createPullRequestPresenter(PullRequestPlace place);

}