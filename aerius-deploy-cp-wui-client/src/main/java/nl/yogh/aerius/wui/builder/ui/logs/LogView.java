package nl.yogh.aerius.wui.builder.ui.logs;

import com.google.inject.ImplementedBy;

import nl.yogh.aerius.wui.builder.ui.logs.LogView.Presenter;
import nl.yogh.gwt.wui.activity.View;

@ImplementedBy(LogViewImpl.class)
public interface LogView extends View<Presenter> {
  public interface Presenter {

  }

}
