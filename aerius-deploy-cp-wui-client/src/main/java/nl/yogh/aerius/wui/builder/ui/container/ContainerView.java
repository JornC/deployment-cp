package nl.yogh.aerius.wui.builder.ui.container;

import java.util.ArrayList;

import com.google.inject.ImplementedBy;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.wui.builder.ui.container.ContainerView.Presenter;
import nl.yogh.gwt.wui.activity.View;

@ImplementedBy(ContainerViewImpl.class)
public interface ContainerView extends View<Presenter> {
  public interface Presenter {

  }

  public void setProducts(ArrayList<ProjectInfo> products);
}
