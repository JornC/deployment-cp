package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ProductEvent extends SimpleGenericEvent<ProductType> {
  private final String hash;

  public ProductEvent(final ProductType type, final String hash) {
    super(type);
    this.hash = hash;
  }

  public String getHash() {
    return hash;
  }
}
