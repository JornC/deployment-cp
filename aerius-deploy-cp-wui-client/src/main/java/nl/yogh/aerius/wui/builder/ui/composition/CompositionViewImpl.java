package nl.yogh.aerius.wui.builder.ui.composition;

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

import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.wui.builder.commands.CompositionStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.component.CompositionViewPanel;
import nl.yogh.gwt.wui.widget.EventComposite;
import nl.yogh.gwt.wui.widget.SwitchPanel;

public class CompositionViewImpl extends EventComposite implements CompositionView {
  private static final ProjectViewImplUiBinder UI_BINDER = GWT.create(ProjectViewImplUiBinder.class);

  interface ProjectViewImplUiBinder extends UiBinder<Widget, CompositionViewImpl> {}

  interface ProjectViewImplEventBinder extends EventBinder<CompositionViewImpl> {}

  private final ProjectViewImplEventBinder EVENT_BINDER = GWT.create(ProjectViewImplEventBinder.class);

  @UiField SwitchPanel switchPanel;
  @UiField FlowPanel panel;

  private final Map<String, CompositionViewPanel> items = new HashMap<>();

  @Inject
  public CompositionViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }

  @EventHandler
  public void onProductStatusInfoChangedEvent(final CompositionStatusInfoChangedEvent e) {
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
  public void setProducts(final ArrayList<CompositionInfo> projects) {
    switchPanel.showWidget(projects.isEmpty() ? 0 : 1);

    for (final CompositionInfo info : projects) {
      panel.add(new CompositionViewPanel(info));
    }
  }
}
