package nl.yogh.aerius.wui.builder;

import nl.yogh.aerius.wui.builder.place.ProjectPlace;
import nl.yogh.aerius.wui.builder.place.DockerPlace;
import nl.yogh.aerius.wui.builder.place.LogPlace;
import nl.yogh.aerius.wui.builder.place.PullRequestPlace;
import nl.yogh.aerius.wui.builder.ui.landing.DockerActivity;
import nl.yogh.aerius.wui.builder.ui.logs.LogActivity;
import nl.yogh.aerius.wui.builder.ui.project.ProjectActivity;
import nl.yogh.aerius.wui.builder.ui.pulls.PullRequestActivity;

public interface BuilderActivityFactory {
  ProjectActivity createContainerPresenter(ProjectPlace place);

  DockerActivity createDockerPresenter(DockerPlace place);

  PullRequestActivity createPullRequestPresenter(PullRequestPlace place);

  LogActivity createLogPresenter(LogPlace place);

}