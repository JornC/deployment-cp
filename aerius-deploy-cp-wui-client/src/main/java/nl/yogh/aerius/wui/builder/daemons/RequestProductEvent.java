package nl.yogh.aerius.wui.builder.daemons;

import java.util.function.Consumer;

import nl.yogh.aerius.builder.domain.ProductInfo;

public class RequestProductEvent extends ResponseEvent<String, ProductInfo> {
  public RequestProductEvent(final String request, final Consumer<ProductInfo> consumer) {
    super(request, consumer);
  }
}
