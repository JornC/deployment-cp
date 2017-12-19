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
import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.PullRequestInfo;
import nl.yogh.aerius.builder.domain.ServiceInfo;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.ProductActionCommand;
import nl.yogh.aerius.wui.builder.commands.ProductStatusInfoChangedEvent;
import nl.yogh.aerius.wui.builder.commands.PullRequestRetrievalEvent;

public class PullRequestRetrievalDaemonImpl extends PullRequestRetrievalPollingAgent {
  interface PullRetrievalDaemonImplEventBinder extends EventBinder<PullRequestRetrievalDaemonImpl> {}

  private final PullRetrievalDaemonImplEventBinder EVENT_BINDER = GWT.create(PullRetrievalDaemonImplEventBinder.class);

  private final PullRequestServiceAsync service;

  @Inject ServiceUpdateDaemon serviceUpdateDaemon;

  @Inject ProductUpdateDaemon productUpdateDaemon;

  private ArrayList<ProductInfo> products;
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
    products = result.getProducts();

    super.start();

    serviceUpdateDaemon.start();
    productUpdateDaemon.start();
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

    e.respond(findServices(e.getValue()));
  }

  private List<ServiceInfo> findServices(final ArrayList<String> value) {
    return findObjects(value, services);
  }

  private ProductInfo findProduct(final String value) {
    return findObject(value, products);
  }

  @EventHandler
  public void onProductActionCommand(final ProductActionCommand c) {
    service.doAction(c.getType(), c.getAction(), c.getValue(), r -> eventBus.fireEvent(new ProductStatusInfoChangedEvent(r)));
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

  private static <T extends HasHash> ArrayList<T> findObjects(final ArrayList<String> value, final ArrayList<T> objects) {
    return objects.stream().filter(v -> value.contains(v.hash())).collect(Collectors.toCollection(ArrayList::new));
  }
}
