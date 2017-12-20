package nl.yogh.aerius.wui.builder.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import nl.yogh.aerius.builder.domain.LogMessage;
import nl.yogh.aerius.wui.util.FormatUtil;

public class LogMessageView extends Composite {
  private static final LogMessageViewUiBinder UI_BINDER = GWT.create(LogMessageViewUiBinder.class);

  interface LogMessageViewUiBinder extends UiBinder<Widget, LogMessageView> {}

  @UiField(provided = true) LogMessage msg;

  @UiField Label time;

  public LogMessageView(final LogMessage msg) {
    this.msg = msg;

    initWidget(UI_BINDER.createAndBindUi(this));

    time.setText(FormatUtil.formatCompactTime(msg.time()));
  }
}
