package nl.yogh.aerius.wui;

import nl.yogh.aerius.wui.resources.R;

public class Application {
  private ApplicationRoot appRoot;

  public Application() {
    R.init();

    onFinishedLoading();
  }

  public static void create() {
    new Application();
  }

  /**
   * Initializes the application activity managers, user interface, and starts
   * the application by handling the current history token.
   */
  private void onFinishedLoading() {
    // Only finish loading once
    if (appRoot != null) {
      return;
    }

    appRoot = new ApplicationRoot();
    try {
      appRoot.startUp();
    } catch (final Exception e) {
      appRoot.hideDisplay();
      throw e;
    }
  }
}