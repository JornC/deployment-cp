package nl.yogh.aerius.wui.builder.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import nl.yogh.aerius.builder.domain.CommitInfo;
import nl.yogh.aerius.builder.domain.CompositionDeploymentAction;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.CompositionStatus;
import nl.yogh.aerius.builder.domain.CompositionType;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.domain.ServiceStatus;
import nl.yogh.aerius.wui.builder.commands.CompositionActionCommand;
import nl.yogh.aerius.wui.builder.commands.CompositionStatusHighlightEvent;
import nl.yogh.aerius.wui.builder.commands.CompositionStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.commands.ServiceStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.daemons.RequestServicesEvent;
import nl.yogh.gwt.wui.widget.EventComposite;

public class CompositionControlButton extends EventComposite {
  private static final CompositionControlButtonUiBinder UI_BINDER = GWT.create(CompositionControlButtonUiBinder.class);

  interface CompositionControlButtonUiBinder extends UiBinder<Widget, CompositionControlButton> {}

  interface CompositionControlButtonEventBinder extends EventBinder<CompositionControlButton> {}

  private final CompositionControlButtonEventBinder EVENT_BINDER = GWT.create(CompositionControlButtonEventBinder.class);

  public interface CustomStyle extends CssResource {
    String running();

    String unbuilt();

    String suspended();

    String disabled();

    String highlight();

    String busy();

    String corrupted();
  }

  @UiField CustomStyle style;

  @UiField SimplePanel container;
  @UiField FlowPanel panel;

  private String activeStatus;

  @UiField(provided = true) CompositionType type;
  @UiField(provided = true) String hash;

  @UiField Label serviceField;
  @UiField Label statusField;

  private final Map<String, ServiceInfo> serviceMap = new HashMap<>();

  private boolean busy;

  private boolean disabled;
  private CompositionInfo info;

  private HandlerRegistration eventRegistration;

  @UiConstructor
  public CompositionControlButton(final CompositionType type, final CommitInfo pull) {
    this.info = pull.compositions() == null ? null : pull.compositions().get(type);
    this.type = type;

    handlePull(pull);
    handleInfo(info);

    panel.addDomHandler(e -> fireHighlight(true), MouseOverEvent.getType());
    panel.addDomHandler(e -> fireHighlight(false), MouseOutEvent.getType());

    panel.addDomHandler(e -> eventBus.fireEvent(new CompositionActionCommand(pull.idx(), info, determineAction(info.status()))),
        ClickEvent.getType());
  }

  private void fireHighlight(final boolean highlight) {
    if (disabled || info == null) {
      return;
    }

    eventBus.fireEvent(new CompositionStatusHighlightEvent(info, highlight));
  }

  private void handlePull(final CommitInfo pull) {
    this.disabled = pull.isBusy();

    this.hash = info == null || disabled ? "N/A" : info.hash();

    initWidget(UI_BINDER.createAndBindUi(this));
  }

  private void handleInfo(final CompositionInfo info) {
    if (info == null) {
      setDisabled();
      return;
    }

    this.info = info;

    setStatusCount(info.services());
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
    if (r != null) {
      for (final ServiceInfo info : r) {
        serviceMap.put(info.hash(), info);
      }
    }

    setStatusCount();
  }

  private void setStatusCount() {
    if (info != null && info.status() == CompositionStatus.CORRUPTED) {
      statusField.setText("Composition is incompatible.");
    } else {
      statusField.setText("compiled: " + getCompileCount());
    }
  }

  @EventHandler
  public void onProductStatusHighlightEvent(final CompositionStatusHighlightEvent e) {
    if (busy) {
      return;
    }

    if (e.getValue().status() == CompositionStatus.CORRUPTED) {
      return;
    }

    if (matches(e.getValue())) {
      highlight(e.isHighlight());
      setDynamicServices(e.getValue(), e.isHighlight());
    }
  }

  private void setDynamicServices(final CompositionInfo value, final boolean highlight) {
    if (highlight) {
      setMatchCount(value);
    } else {
      setServiceCount();
    }
  }

  private void setServiceCount() {
    if (info.status() == CompositionStatus.CORRUPTED) {
      serviceField.setText("");
    } else {
      serviceField.setText("services: " + getServiceCount());
    }
  }

  private void setMatchCount(final CompositionInfo value) {
    final long matchCount = getMatchCount(value);

    serviceField.setText("matches: " + matchCount + "/" + getServiceCount());
  }

  private int getServiceCount() {
    return info == null || info.services() == null ? 0 : info.services().size();
  }

  private String getCompileCount() {
    return String
        .valueOf(serviceMap.values().stream().filter(e -> e.status() == ServiceStatus.BUILT).count());
  }

  private boolean matches(final CompositionInfo value) {
    final long matchCount = getMatchCount(value);
    return value.status() == CompositionStatus.UNBUILT && matchCount > 0 || matchCount == value.services().size();
  }

  private long getMatchCount(final CompositionInfo value) {
    if (info == null || info.services() == null) {
      return 0;
    }

    int count = 0;
    for (final ServiceInfo compositionHash : info.services()) {
      for (final ServiceInfo otherHash : value.services()) {
        if (otherHash.hash().equals(compositionHash.hash())) {
          count++;
        }
      }
    }

    return count;
  }

  @EventHandler
  public void onServiceStatusInfoChangedEvent(final ServiceStatusInfoChangedEvent e) {
    if (info == null || !info.services().stream().map(v -> v.hash()).collect(Collectors.toList()).contains(e.getValue().hash())) {
      return;
    }

    serviceMap.put(e.getValue().hash(), e.getValue());
    updateStatusCount();
  }

  @EventHandler
  public void onProductStatusInfoChangedEvent(final CompositionStatusInfoChangedEvent e) {
    if (disabled) {
      return;
    }

    final CompositionInfo info = e.getValue();

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

  private void setStatus(final CompositionStatus status) {
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
    case CORRUPTED:
      setActiveStatus(style.corrupted());
      break;
    default:
      setDisabled();
      break;
    }
  }

  private static CompositionDeploymentAction determineAction(final CompositionStatus status) {
    switch (status) {
    case DEPLOYED:
      return CompositionDeploymentAction.SUSPEND;
    case SUSPENDED:
      return CompositionDeploymentAction.DEPLOY;
    case CORRUPTED:
      return null;
    default:
    case UNBUILT:
      return CompositionDeploymentAction.BUILD;
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
