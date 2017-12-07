package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.builder.service.ProductDeploymentAction;
import nl.yogh.aerius.wui.builder.commands.ProductActionCommand;
import nl.yogh.aerius.wui.builder.commands.ProductStatusHighlightEvent;
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

    String disabled();

    String highlight();

    String busy();
  }

  @UiField CustomStyle style;

  @UiField FlowPanel panel;

  private String activeStatus;

  @UiField(provided = true) ProductType type;
  @UiField(provided = true) String hash;

  private final ProductInfo info;

  @UiConstructor
  public ProjectControlButton(final ProductType type, final PullRequestInfo pull) {
    this.info = pull.products().get(type);

    this.type = type;
    this.hash = info == null ? "N/A" : info.hash();

    initWidget(UI_BINDER.createAndBindUi(this));

    handleProductInfo(info);

    panel.addDomHandler(e -> eventBus.fireEvent(new ProductStatusHighlightEvent(info, true)), MouseOverEvent.getType());
    panel.addDomHandler(e -> eventBus.fireEvent(new ProductStatusHighlightEvent(info, false)), MouseOutEvent.getType());

    panel.addDomHandler(e -> eventBus.fireEvent(new ProductActionCommand(info, type, determineAction(info.status()))), ClickEvent.getType());
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    eventBinder.bindEventHandlers(this, eventBus);
  }

  @EventHandler
  public void onProductStatusHighlightEvent(final ProductStatusHighlightEvent e) {
    if (e.getValue().hash().equals(hash)) {
      highlight(e.isHighlight());
    }
  }

  @EventHandler
  public void onProductStatusInfoChangedEvent(final ProductStatusInfoChangedEvent e) {
    final ProductInfo info = e.getValue();

    if (!info.hash().equals(hash)) {
      return;
    }

    handleInfo(info);
  }

  private void handleInfo(final ProductInfo info) {
    setStatus(info.status());
    setBusy(info.busy());
  }

  private void setBusy(final boolean busy) {
    if (busy) {
      highlight(false);
    }

    setStyleName(style.busy(), busy);
  }

  private void highlight(final boolean highlight) {
    setStyleName(style.highlight(), highlight);
  }

  private void handleProductInfo(final ProductInfo info) {
    if (info == null) {
      setDisabled();
      return;
    }

    setStatus(info.status());
  }

  private void setStatus(final ServiceStatus status) {
    if (status == null) {
      setDisabled();
      return;
    }

    switch (status) {
    case RUNNING:
      setActiveStatus(style.running());
      break;
    case SUSPENDED:
      setActiveStatus(style.suspended());
      break;
    case UNBUILT:
      setActiveStatus(style.unbuilt());
      break;
    default:
      setDisabled();
      break;
    }
  }

  private static ProductDeploymentAction determineAction(final ServiceStatus status) {
    switch (status) {
    case RUNNING:
      return ProductDeploymentAction.SUSPEND;
    case SUSPENDED:
      return ProductDeploymentAction.DEPLOY;
    default:
    case UNBUILT:
      return ProductDeploymentAction.BUILD;
    }
  }

  private void setDisabled() {
    setActiveStatus(style.disabled());
  }

  private void setActiveStatus(final String status) {
    if (activeStatus != null) {
      removeStyleName(activeStatus);
    }

    addStyleName(status);
    activeStatus = status;
  }
}
