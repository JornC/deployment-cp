package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SimpleStatView extends Composite {
  private static final SimpleStatViewUiBinder UI_BINDER = GWT.create(SimpleStatViewUiBinder.class);

  interface SimpleStatViewUiBinder extends UiBinder<Widget, SimpleStatView> {}

  @UiField(provided = true) String key;
  @UiField(provided = true) String value;

  public SimpleStatView(final String key, final String value) {
    this.key = key;
    this.value = value;

    initWidget(UI_BINDER.createAndBindUi(this));
  }
}
