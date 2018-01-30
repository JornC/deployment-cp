package nl.yogh.aerius.wui.builder.ui.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.wui.builder.commands.ProjectStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.component.ProjectViewPanel;
import nl.yogh.gwt.wui.widget.EventComposite;

public class ContainerViewImpl extends EventComposite implements ContainerView {
  private static final ContainerViewImplUiBinder UI_BINDER = GWT.create(ContainerViewImplUiBinder.class);

  interface ContainerViewImplUiBinder extends UiBinder<Widget, ContainerViewImpl> {}

  interface ContainerViewImplEventBinder extends EventBinder<ContainerViewImpl> {}

  private final ContainerViewImplEventBinder EVENT_BINDER = GWT.create(ContainerViewImplEventBinder.class);

  @UiField FlowPanel panel;

  private final Map<String, ProjectViewPanel> items = new HashMap<>();

  @Inject
  public ContainerViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @EventHandler
  public void onProductStatusInfoChangedEvent(final ProjectStatusInfoChangedEvent e) {
    final String hash = e.getValue().hash();
    if (!items.containsKey(hash)) {
      GWT.log("Error: ContainerViewImpl.onProductStatusInfoChangedEvent this item should exist." + e.getValue());
      return;
    }

    items.get(hash).update(e.getValue());
  }

  @Override
  protected void onLoad() {}

  @Override
  public void setPresenter(final Presenter presenter) {}

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @Override
  public void setProducts(final ArrayList<ProjectInfo> projects) {
    for (final ProjectInfo info : projects) {
      panel.add(new ProjectViewPanel(info));
    }
  }
}
