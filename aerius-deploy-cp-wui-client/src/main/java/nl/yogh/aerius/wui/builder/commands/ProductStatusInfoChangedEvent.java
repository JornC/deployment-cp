package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProductType;

public class ProductStatusInfoChangedEvent extends ProductEvent {
  public ProductStatusInfoChangedEvent(final ProductType type, final String hash) {
    super(type, hash);
  }
}
