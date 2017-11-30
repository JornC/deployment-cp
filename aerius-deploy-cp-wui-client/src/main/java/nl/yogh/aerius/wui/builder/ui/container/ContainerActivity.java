package nl.yogh.aerius.wui.builder.ui.container;

import javax.inject.Inject;

import nl.yogh.aerius.wui.builder.ui.container.ContainerView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;

public class ContainerActivity extends EventActivity<Presenter, ContainerView> implements Presenter {

  @Inject
  public ContainerActivity(final ContainerView view) {
    super(view);
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
