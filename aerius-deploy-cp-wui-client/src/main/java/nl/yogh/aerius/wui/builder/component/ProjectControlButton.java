package nl.yogh.aerius.wui.builder.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.ProjectDeploymentAction;
import nl.yogh.aerius.builder.domain.ProjectInfo;
import nl.yogh.aerius.builder.domain.ProjectType;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.wui.builder.commands.ProjectActionCommand;
import nl.yogh.aerius.wui.builder.commands.ProjectStatusHighlightEvent;
import nl.yogh.aerius.wui.builder.commands.ProjectStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.commands.ServiceStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.daemons.RequestServicesEvent;
import nl.yogh.gwt.wui.widget.EventComposite;

public class ProjectControlButton extends EventComposite {
  interface ProjectControlButtonEventBinder extends EventBinder<ProjectControlButton> {}

  private final ProjectControlButtonEventBinder EVENT_BINDER = GWT.create(ProjectControlButtonEventBinder.class);

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

  @UiField SimplePanel container;
  @UiField FlowPanel panel;

  private String activeStatus;

  @UiField(provided = true) ProjectType type;
  @UiField(provided = true) String hash;

  @UiField Label serviceField;
  @UiField Label statusField;

  private final Map<String, ServiceInfo> serviceMap = new HashMap<>();

  private boolean busy;

  private boolean disabled;
  private ProjectInfo info;

  private HandlerRegistration eventRegistration;

  @UiConstructor
  public ProjectControlButton(final ProjectType type, final PullRequestInfo pull) {
    this.info = pull.projects() == null ? null : pull.projects().get(type);
    this.type = type;

    handlePull(pull);
    handleInfo(info);

    panel.addDomHandler(e -> fireHighlight(true), MouseOverEvent.getType());
    panel.addDomHandler(e -> fireHighlight(false), MouseOutEvent.getType());

    panel.addDomHandler(e -> eventBus.fireEvent(new ProjectActionCommand(pull.idx(), info, type, determineAction(info.status()))),
        ClickEvent.getType());
  }

  private void fireHighlight(final boolean highlight) {
    if (disabled || info == null) {
      return;
    }

    eventBus.fireEvent(new ProjectStatusHighlightEvent(info, highlight));
  }

  private void handlePull(final PullRequestInfo pull) {
    this.disabled = pull.isBusy();

    this.hash = info == null || disabled ? "N/A" : info.hash();

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  private void handleInfo(final ProjectInfo info) {
    if (info == null) {
      setDisabled();
      return;
    }

    this.info = info;

    setStatus(info.status());
    setBusy(info.busy());
    setServiceCount();
    setStatusCount();
  }

  @Override
  protected void onUnload() {
    eventRegistration.removeHandler();
  }

  @Override
  public void setEventBus(final EventBus eventBus) {
    super.setEventBus(eventBus);

    eventRegistration = EVENT_BINDER.bindEventHandlers(this, eventBus);

    setStatusCount();
  }

  private void updateStatusCount() {
    eventBus.fireEvent(new RequestServicesEvent(info.services(), r -> setStatusCount(r)));
  }

  private void setStatusCount(final List<ServiceInfo> r) {
    for (final ServiceInfo info : r) {
      serviceMap.put(info.hash(), info);
    }

    setStatusCount();
  }

  private void setStatusCount() {
    statusField.setText("compiled: " + getDeployedOrRunningCount());
  }

  @EventHandler
  public void onProductStatusHighlightEvent(final ProjectStatusHighlightEvent e) {
    if (busy) {
      return;
    }

    if (matches(e.getValue())) {
      highlight(e.isHighlight());
      setDynamicServices(e.getValue(), e.isHighlight());
    }
  }

  private void setDynamicServices(final ProjectInfo value, final boolean highlight) {
    if (highlight) {
      setMatchCount(value);
    } else {
      setServiceCount();
    }
  }

  private void setServiceCount() {
    serviceField.setText("services: " + getServiceCount());
  }

  private void setMatchCount(final ProjectInfo value) {
    final long matchCount = getMatchCount(value);

    serviceField.setText("matches: " + matchCount + "/" + getServiceCount());
  }

  private int getServiceCount() {
    return info == null || info.services() == null ? 0 : info.services().size();
  }

  private String getDeployedOrRunningCount() {
    return String
        .valueOf(serviceMap.values().stream().filter(e -> e.status() == ServiceStatus.DEPLOYED || e.status() == ServiceStatus.SUSPENDED).count());
  }

  private boolean matches(final ProjectInfo value) {
    final long matchCount = getMatchCount(value);
    return value.status() == ServiceStatus.UNBUILT && matchCount > 0 || matchCount == value.services().size();
  }

  private long getMatchCount(final ProjectInfo value) {
    if (info == null || info.services() == null) {
      return 0;
    }

    int count = 0;
    for (final String projectHash : info.services()) {
      for (final String otherHash : value.services()) {
        if (otherHash.equals(projectHash)) {
          count++;
        }
      }
    }

    return count;
  }

  @EventHandler
  public void onServiceStatusInfoChangedEvent(final ServiceStatusInfoChangedEvent e) {
    if (info == null || !info.services().contains(e.getValue().hash())) {
      return;
    }

    serviceMap.put(e.getValue().hash(), e.getValue());
    updateStatusCount();
  }

  @EventHandler
  public void onProductStatusInfoChangedEvent(final ProjectStatusInfoChangedEvent e) {
    if (disabled) {
      return;
    }

    final ProjectInfo info = e.getValue();

    if (!info.hash().equals(hash)) {
      return;
    }

    handleInfo(info);
  }

  private void setBusy(final boolean busy) {
    this.busy = busy;

    if (busy) {
      highlight(false);
    }

    container.setStyleName(style.busy(), busy);
  }

  private void highlight(final boolean highlight) {
    panel.setStyleName(style.highlight(), highlight);
  }

  private void setStatus(final ServiceStatus status) {
    if (status == null || disabled) {
      setDisabled();
      return;
    }

    switch (status) {
    case DEPLOYED:
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

  private static ProjectDeploymentAction determineAction(final ServiceStatus status) {
    switch (status) {
    case DEPLOYED:
      return ProjectDeploymentAction.SUSPEND;
    case SUSPENDED:
      return ProjectDeploymentAction.DEPLOY;
    default:
    case UNBUILT:
      return ProjectDeploymentAction.BUILD;
    }
  }

  private void setDisabled() {
    setActiveStatus(style.disabled());
  }

  private void setActiveStatus(final String status) {
    if (activeStatus != null) {
      panel.removeStyleName(activeStatus);
    }

    panel.addStyleName(status);
    activeStatus = status;
  }
}
