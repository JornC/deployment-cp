package nl.yogh.aerius.wui.builder.ui.landing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.yogh.aerius.builder.domain.DockerContainer;
import nl.yogh.aerius.builder.domain.DockerImage;
import nl.yogh.aerius.wui.builder.component.ContainerViewPanel;
import nl.yogh.aerius.wui.builder.component.ImageViewPanel;
import nl.yogh.aerius.wui.builder.component.SimpleStatView;
import nl.yogh.gwt.wui.widget.EventComposite;
import nl.yogh.gwt.wui.widget.SwitchPanel;

public class DockerViewImpl extends EventComposite implements DockerView {
  private static final LandingViewImplUiBinder UI_BINDER = GWT.create(LandingViewImplUiBinder.class);

  interface LandingViewImplUiBinder extends UiBinder<Widget, DockerViewImpl> {}

  private final LandingViewImplEventBinder EVENT_BINDER = GWT.create(LandingViewImplEventBinder.class);

  interface LandingViewImplEventBinder extends EventBinder<DockerViewImpl> {}

  private Presenter presenter;

  @UiField SwitchPanel containerSwitchPanel;
  @UiField FlowPanel containerPanel;
  @UiField SwitchPanel imageSwitchPanel;
  @UiField FlowPanel imagePanel;

  @UiField FlowPanel statsPanel;

  @Inject
  public DockerViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));

    containerSwitchPanel.showWidget(0);
    imageSwitchPanel.showWidget(0);
  }

  @UiHandler("stopAllContainers")
  public void onStopAllContainers(final ClickEvent e) {
    presenter.stopAllContainers();
  }

  @UiHandler("removeAllContainers")
  public void onRemoveAllContainers(final ClickEvent e) {
    presenter.removeAllContainers();
  }

  @UiHandler("removeAllImages")
  public void onRemoveAllImages(final ClickEvent e) {
    presenter.removeAllImages();
  }

  @UiHandler("purgeTracker")
  public void onPurgeTracker(final ClickEvent e) {
    presenter.purgeTracker();
  }

  @Override
  public void setStats(final HashMap<String, String> stats) {
    statsPanel.clear();

    for (final Entry<String, String> entry : stats.entrySet()) {
      statsPanel.add(new SimpleStatView(entry.getKey(), entry.getValue()));
    }
  }

  @Override
  protected void onLoad() {}

  @Override
  public void setPresenter(final Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @Override
  public void setContainers(final ArrayList<DockerContainer> containers) {
    if (containers == null) {
      return;
    }

    containerSwitchPanel.showWidget(containers.isEmpty() ? 0 : 1);
    containerPanel.clear();
    containers.forEach(v -> containerPanel.add(new ContainerViewPanel(v, presenter)));
  }

  @Override
  public void setImages(final ArrayList<DockerImage> images) {
    if (images == null) {
      return;
    }

    imageSwitchPanel.showWidget(images.isEmpty() ? 0 : 1);
    imagePanel.clear();
    images.forEach(v -> imagePanel.add(new ImageViewPanel(v, presenter)));
  }
}
