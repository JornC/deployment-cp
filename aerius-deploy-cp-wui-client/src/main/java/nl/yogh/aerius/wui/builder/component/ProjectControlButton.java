package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.wui.builder.commands.ProductStatusInfoChangedEvent;
import nl.yogh.gwt.wui.widget.EventComposite;

public class ProjectControlButton extends EventComposite {
  interface ProjectControlButtonEventBinder extends EventBinder<ProjectControlButton> {}

  private final ProjectControlButtonEventBinder eventBinder = GWT.create(ProjectControlButtonEventBinder.class);

  interface ProjectControlButtonUiBinder extends UiBinder<Widget, ProjectControlButton> {}

  private static final ProjectControlButtonUiBinder UI_BINDER = GWT.create(ProjectControlButtonUiBinder.class);

  public interface CustomStyle extends CssResource {
    String running();

    String unbuilt();

    String suspended();
  }

  @UiField FlowPanel panel;
  @UiField Label textField;

  private final PullRequestInfo pull;
  private final ProductType type;

  @UiConstructor
  public ProjectControlButton(final ProductType type, final PullRequestInfo pull) {
    this.type = type;
    this.pull = pull;

    initWidget(UI_BINDER.createAndBindUi(this));

    textField.setText(type.name());
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    eventBinder.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onPullRequestInfoChanged(final ProductStatusInfoChangedEvent e) {

  }
}
