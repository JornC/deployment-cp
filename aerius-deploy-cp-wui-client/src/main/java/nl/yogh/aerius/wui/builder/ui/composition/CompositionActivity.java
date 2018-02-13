package nl.yogh.aerius.wui.builder.ui.composition;

import javax.inject.Inject;

import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.CompositionRetrievalDeactivationCommand;
import nl.yogh.aerius.wui.builder.ui.composition.CompositionView.Presenter;
import nl.yogh.aerius.wui.builder.commands.CompositionRetrievalActivationCommand;
import nl.yogh.gwt.wui.activity.EventActivity;

public class CompositionActivity extends EventActivity<Presenter, CompositionView> implements Presenter {
  private final PullRequestServiceAsync service;

  @Inject
  public CompositionActivity(final CompositionView view, final PullRequestServiceAsync service) {
    super(view);
    this.service = service;
  }

  @Override
  protected void onStart() {
    service.getPresentSituation(result -> handlePresent(result));

  }

  private void handlePresent(final PresentSnapshot result) {
    view.setProducts(result.getProjects());
    eventBus.fireEvent(new CompositionRetrievalActivationCommand());
  }

  @Override
  public void onStop() {
    eventBus.fireEvent(new CompositionRetrievalDeactivationCommand());
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
