package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProductInfo;

public class ProductStatusInfoChangedEvent extends ProductEvent {
  public ProductStatusInfoChangedEvent(final ProductInfo info) {
    super(info);
  }
}
