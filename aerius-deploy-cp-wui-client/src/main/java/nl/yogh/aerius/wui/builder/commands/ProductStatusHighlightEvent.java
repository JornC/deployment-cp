package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProductInfo;

public class ProductStatusHighlightEvent extends ProductEvent {
  private final boolean highlight;

  public ProductStatusHighlightEvent(final ProductInfo info, final boolean highlight) {
    super(info);
    this.highlight = highlight;
  }

  public boolean isHighlight() {
    return highlight;
  }
}
