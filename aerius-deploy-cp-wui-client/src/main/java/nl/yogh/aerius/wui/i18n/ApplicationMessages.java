package nl.yogh.aerius.wui.i18n;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

import nl.yogh.aerius.wui.util.ApplicationConstants;

/**
 * GWT interface to language specific static text.
 */
@DefaultLocale(ApplicationConstants.DEFAULT_LOCALE)
public interface ApplicationMessages extends Messages {
  @Description("A fatal error occurred, most likely a bug.")
  String errorInternalFatal();

  String siteTitle();

  String placeButtonText(@Select String simpleName);

  String placeTitle(@Select String simpleName);
}
