package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nl.yogh.gwt.wui.widget.EventComposite;

public class ProjectControlButton extends EventComposite implements HasText {
  private static final ProjectControlButtonUiBinder UI_BINDER = GWT.create(ProjectControlButtonUiBinder.class);

  interface ProjectControlButtonUiBinder extends UiBinder<Widget, ProjectControlButton> {}

  @UiField Label textField;

  public ProjectControlButton() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @Override
  public String getText() {
    return textField.getText();
  }

  @Override
  public void setText(final String text) {
    textField.setText(text);
  }
}
