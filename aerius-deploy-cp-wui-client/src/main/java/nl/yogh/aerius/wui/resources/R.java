package nl.yogh.aerius.wui.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Global resource class, access resources via R.css(). or R.images().
 */
public final class R {

  /**
   * Aerius CssResources.
   */
  public interface ApplicationCssResource extends CssResource {
    String bodyLight();

    String bodyDark();

    String rootLayoutPanel();

    String glassPanel();

    String removeGlassPanel();

    String elementMargin();

    String flex();

    String columns();

    String justify();

    String alignCenter();

    String distribute();

    String around();

    String line();

    String grow();

    String noShrink();

    String rowReverse();

    String clickableBackground();

    String globalMaxWidth();

    String focus();

    String wrap();

    String wrapMargins();

    String mainPanel();

    String pageTitle();
  }

  public interface ApplicationResource extends ClientBundle, ImageResources {
    @Source("strict.gss")
    ApplicationCssResource css();
  }

  private static final ApplicationResource RESOURCES = GWT.create(ApplicationResource.class);

  // Don't instantiate directly, use the static fields.
  private R() {}

  /**
   * Ensures css is injected. Should be called as soon as possible on startup.
   */
  public static void init() {
    RESOURCES.css().ensureInjected();
  }

  /**
   * Access to css resources.
   */
  public static ApplicationCssResource css() {
    return RESOURCES.css();
  }

  /**
   * Access to image resources.
   */
  public static ImageResources images() {
    return RESOURCES;
  }
}
