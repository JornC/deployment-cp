package nl.yogh.aerius.wui.builder.ui.project;

import javax.inject.Inject;

import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.ProjectRetrievalDeactivationCommand;
import nl.yogh.aerius.wui.builder.ui.project.ProjectView.Presenter;
import nl.yogh.gwt.wui.activity.EventActivity;

public class ProjectActivity extends EventActivity<Presenter, ProjectView> implements Presenter {
  private final PullRequestServiceAsync service;

  @Inject
  public ProjectActivity(final ProjectView view, final PullRequestServiceAsync service) {
    super(view);
    this.service = service;
  }

  @Override
  protected void onStart() {
    service.getPresentSituation(result -> handlePresent(result));

  }

  private void handlePresent(final PresentSnapshot result) {
    view.setProducts(result.getProjects());
    // eventBus.fireEvent(new ProjectRetrievalActivationCommand());
  }

  @Override
  public void onStop() {
    eventBus.fireEvent(new ProjectRetrievalDeactivationCommand());
  }

  @Override
  public Presenter getPresenter() {
    return this;
  }
}
