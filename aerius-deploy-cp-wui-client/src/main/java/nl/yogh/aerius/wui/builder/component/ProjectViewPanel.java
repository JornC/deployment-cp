package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import nl.yogh.aerius.builder.domain.ProjectInfo;

public class ProjectViewPanel extends Composite {
  private static final ProjectViewPanelUiBinder UI_BINDER = GWT.create(ProjectViewPanelUiBinder.class);

  interface ProjectViewPanelUiBinder extends UiBinder<Widget, ProjectViewPanel> {}

  @UiField(provided = true) ProjectInfo project;

  public ProjectViewPanel(final ProjectInfo info) {
    this.project = info;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  public void update(final ProjectInfo value) {
    throw new RuntimeException("Not supporting updates.");
  }
}
