package nl.yogh.aerius.wui.builder.commands;

import nl.yogh.aerius.builder.domain.ProductInfo;
import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.service.ProductDeploymentAction;
import nl.yogh.gwt.wui.event.SimpleGenericEvent;

public class ProductActionCommand extends SimpleGenericEvent<ProductInfo> {
  private final ProductType type;
  private final ProductDeploymentAction action;

  public ProductActionCommand(final ProductInfo info, final ProductType type, final ProductDeploymentAction action) {
    super(info);
    this.type = type;
    this.action = action;
  }

  public ProductType getType() {
    return type;
  }

  public ProductDeploymentAction getAction() {
    return action;
  }
}
