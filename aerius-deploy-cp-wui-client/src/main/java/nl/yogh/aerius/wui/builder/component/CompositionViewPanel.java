package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.gwt.wui.widget.SwitchPanel;

public class CompositionViewPanel extends Composite {
  private static final ProjectViewPanelUiBinder UI_BINDER = GWT.create(ProjectViewPanelUiBinder.class);

  interface ProjectViewPanelUiBinder extends UiBinder<Widget, CompositionViewPanel> {}

  @UiField(provided = true) CompositionInfo composition;

  @UiField SwitchPanel switchPanel;

  public CompositionViewPanel(final CompositionInfo info) {
    this.composition = info;

    if (info.url() == null) {
      info.url("Pending...");
    }

    initWidget(UI_BINDER.createAndBindUi(this));

    switchPanel.showWidget(info.busy() ? 0 : 1);
  }

  public void update(final CompositionInfo value) {
    throw new RuntimeException("Not supporting updates.");
  }
}
