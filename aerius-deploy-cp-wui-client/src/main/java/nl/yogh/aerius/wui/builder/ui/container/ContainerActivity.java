package nl.yogh.aerius.wui.builder.ui.container;

import javax.inject.Inject;

import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalDeactivationCommand;
import nl.yogh.aerius.wui.builder.ui.container.ContainerView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;

public class ContainerActivity extends EventActivity<Presenter, ContainerView> implements Presenter {
  private final PullRequestServiceAsync service;

  @Inject
  public ContainerActivity(final ContainerView view, final PullRequestServiceAsync service) {
    super(view);
    this.service = service;
  }

  @Override
  protected void onStart() {
    service.getPresentSituation(result -> handlePresent(result));

  }

  private void handlePresent(final PresentSnapshot result) {
    view.setProducts(result.getProjects());
    // eventBus.fireEvent(new PullRequestRetrievalActivationCommand());
  }

  @Override
  public void onStop() {
    eventBus.fireEvent(new PullRequestRetrievalDeactivationCommand());
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
