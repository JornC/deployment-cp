package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProductType;

public class ProductStatusHighlightEvent extends ProductEvent {
  public ProductStatusHighlightEvent(final ProductType type, final String hash) {
    super(type, hash);
  }
}
