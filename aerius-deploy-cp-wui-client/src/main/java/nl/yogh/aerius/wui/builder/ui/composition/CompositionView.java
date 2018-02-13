package nl.yogh.aerius.wui.builder.ui.composition;

import java.util.ArrayList;

import com.google.inject.ImplementedBy;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.wui.builder.ui.composition.CompositionView.Presenter;
import nl.yogh.gwt.wui.activity.View;

@ImplementedBy(CompositionViewImpl.class)
public interface CompositionView extends View<Presenter> {
  public interface Presenter {
  }

  public void setProducts(ArrayList<CompositionInfo> products);
}
