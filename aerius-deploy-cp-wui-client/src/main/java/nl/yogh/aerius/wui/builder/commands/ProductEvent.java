package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ProductEvent extends SimpleGenericEvent<ProductInfo> {
  public ProductEvent(final ProductInfo type) {
    super(type);
  }
}
