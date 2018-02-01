package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import nl.yogh.aerius.builder.domain.DockerImage;
import nl.yogh.aerius.wui.builder.ui.landing.DockerView.Presenter;

public class ImageViewPanel extends Composite {
  private static final ProjectViewPanelUiBinder UI_BINDER = GWT.create(ProjectViewPanelUiBinder.class);

  interface ProjectViewPanelUiBinder extends UiBinder<Widget, ImageViewPanel> {}

  @UiField(provided = true) DockerImage info;
  private final Presenter presenter;

  public ImageViewPanel(final DockerImage info, final Presenter presenter) {
    this.info = info;
    this.presenter = presenter;

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @UiHandler("removeButton")
  public void onRemoveClick(final ClickEvent e) {
    presenter.removeImage(info, v -> {
      if (v) {
        removeFromParent();
      }
    });
  }
}
