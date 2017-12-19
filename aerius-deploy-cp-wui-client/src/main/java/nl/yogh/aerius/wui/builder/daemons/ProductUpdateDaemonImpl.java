package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.service.PullRequestServiceAsync;
import nl.yogh.aerius.wui.builder.commands.ProductStatusInfoChangedEvent;

public class ProductUpdateDaemonImpl extends ProductUpdatePollingAgent {
  interface ProductUpdateDaemonImplEventBinder extends EventBinder<ProductUpdateDaemonImpl> {}

  private final ProductUpdateDaemonImplEventBinder EVENT_BINDER = GWT.create(ProductUpdateDaemonImplEventBinder.class);

  private final PullRequestServiceAsync service;

  private long lastUpdate;

  @Inject
  public ProductUpdateDaemonImpl(final EventBus eventBus, final PullRequestServiceAsync service) {
    super(eventBus);

    this.service = service;

    EVENT_BINDER.bindEventHandlers(this, eventBus);
  }

  @Override
  protected void fetch(final AsyncCallback<ArrayList<ProductInfo>> callback) {
    GWT.log("Asking product updates since: " + lastUpdate);
    service.getProductUpdates(lastUpdate, callback);
    lastUpdate = new Date().getTime();
  }

  @Override
  protected void handleResult(final ArrayList<ProductInfo> result) {
    GWT.log("Got product updates: " + result.size());
    for (final ProductInfo info : result) {
      eventBus.fireEvent(new ProductStatusInfoChangedEvent(info));
    }
  }
}
