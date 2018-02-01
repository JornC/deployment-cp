package nl.yogh.aerius.wui.builder.ui.project;

import java.util.ArrayList;

import com.google.inject.ImplementedBy;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.wui.builder.ui.project.ProjectView.Presenter;
import nl.yogh.gwt.wui.activity.View;

@ImplementedBy(ProjectViewImpl.class)
public interface ProjectView extends View<Presenter> {
  public interface Presenter {
  }

  public void setProducts(ArrayList<ProjectInfo> products);
}
