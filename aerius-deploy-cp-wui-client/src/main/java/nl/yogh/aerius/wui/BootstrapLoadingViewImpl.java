package nl.yogh.aerius.wui;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BootstrapLoadingViewImpl extends Composite {
  private static final BootstrapLoadingViewUiBinder UI_BINDER = GWT.create(BootstrapLoadingViewUiBinder.class);

  interface BootstrapLoadingViewUiBinder extends UiBinder<Widget, BootstrapLoadingViewImpl> {}

  public BootstrapLoadingViewImpl() {
    initWidget(UI_BINDER.createAndBindUi(this));
  }
}
