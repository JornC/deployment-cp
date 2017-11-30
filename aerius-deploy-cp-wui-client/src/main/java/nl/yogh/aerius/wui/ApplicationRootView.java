package nl.yogh.aerius.wui;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.ImplementedBy;

import nl.yogh.aerius.wui.builder.ui.ApplicationRootViewImpl;

/**
 * Main Application Display interface.
 */
@ImplementedBy(ApplicationRootViewImpl.class)
public interface ApplicationRootView extends IsWidget, AcceptsOneWidget {}
