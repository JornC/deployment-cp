package nl.yogh.aerius.wui;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Bootstrap implements EntryPoint {

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(new BootstrapLoadingViewImpl());

    GWT.log(new Date().getTime() + " <Bootstrap start time.");

    GWT.runAsync(new RunAsyncCallback() {
      @Override
      public void onFailure(final Throwable caught) {
        GWT.log("Bootstrap failed. " + caught.getMessage());
      }

      @Override
      public void onSuccess() {
        GWT.log(new Date().getTime() + " <Application load time.");
        RootPanel.get().clear();

        Application.create();

        GWT.log(new Date().getTime() + " <Application loaded time.");
      }
    });
  }

}