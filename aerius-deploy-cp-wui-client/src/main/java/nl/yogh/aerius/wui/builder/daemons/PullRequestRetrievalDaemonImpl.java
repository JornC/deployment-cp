package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import nl.yogh.aerius.builder.domain.HasHash;
import nl.yogh.aerius.builder.domain.PresentSnapshot;
import nl.yogh.aerius.builder.domain.CompositionInfo;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.CompositionActionCommand;
import nl.yogh.aerius.wui.builder.commands.CompositionStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalEvent;

public class PullRequestRetrievalDaemonImpl extends PullRequestRetrievalPollingAgent {
  interface PullRetrievalRetrievalDaemonImplEventBinder extends EventBinder<PullRequestRetrievalDaemonImpl> {}

  private final PullRetrievalRetrievalDaemonImplEventBinder EVENT_BINDER = GWT.create(PullRetrievalRetrievalDaemonImplEventBinder.class);

  private final PullRequestServiceAsync service;

  @Inject ServiceUpdateDaemon serviceUpdateDaemon;
  @Inject CompositionUpdateDaemon productUpdateDaemon;

  private ArrayList<CompositionInfo> products;
  private ArrayList<ServiceInfo> services;

  @Inject
  public PullRequestRetrievalDaemonImpl(final EventBus eventBus, final PullRequestServiceAsync service) {
    super(eventBus);

    this.service = service;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @Override
  public void start() {
    service.getPresentSituation(result -> handlePresent(result));
  }

  private void handlePresent(final PresentSnapshot result) {
    services = result.getServices();
    products = result.getProjects();

    super.start();

    serviceUpdateDaemon.start();
    productUpdateDaemon.start();

    super.stop();
  }

  @Override
  public void stop() {
    super.stop();

    serviceUpdateDaemon.stop();
    productUpdateDaemon.stop();
  }

  @EventHandler
  public void onRequestProductEvent(final RequestProductEvent e) {
    if (services == null) {
      e.fail();
    }

    e.respond(findProduct(e.getValue()));
  }

  @EventHandler
  public void onRequestServicesEvent(final RequestServicesEvent e) {
    if (services == null) {
      e.fail();
    }

    e.respond(findServices(e.getValue().stream().map(v -> v.hash()).collect(Collectors.toList())));
  }

  private List<ServiceInfo> findServices(final List<String> value) {
    GWT.log("Finding services: " + value);
    return findObjects(value, services);
  }

  private CompositionInfo findProduct(final String value) {
    return findObject(value, products);
  }

  @EventHandler
  public void onProductActionCommand(final CompositionActionCommand c) {
    service.doAction(c.getIdx(), c.getAction(), c.getValue(), r -> eventBus.fireEvent(new CompositionStatusInfoChangedEvent(r)));
  }

  @Override
  protected void handleResult(final ArrayList<PullRequestInfo> c) {
    eventBus.fireEvent(new PullRequestRetrievalEvent(c));
  }

  @Override
  protected void fetch(final AsyncCallback<ArrayList<PullRequestInfo>> callback) {
    service.getPullRequests(callback);
  }

  private static <T extends HasHash> T findObject(final String value, final ArrayList<T> objects) {
    return objects.stream().filter(v -> v.hash().equals(value)).findFirst().orElse(null);
  }

  private static <T extends HasHash> ArrayList<T> findObjects(final List<String> value, final ArrayList<T> objects) {
    return objects.stream().filter(v -> value.contains(v.hash())).collect(Collectors.toCollection(ArrayList::new));
  }
}
