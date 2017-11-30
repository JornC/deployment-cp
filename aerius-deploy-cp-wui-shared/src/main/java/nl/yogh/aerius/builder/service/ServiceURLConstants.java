package nl.yogh.aerius.builder.service;

public final class ServiceURLConstants {
  private ServiceURLConstants() {}

  /**
   * Base path for all services.
   */
  public static final String BASE_JSON_PATH = "json";

  /**
   * Base path for all services.
   */
  public static final String BASE_GWT_PATH = "gwt";

  /**
   * URL pattern for service dispatcher servlets.
   */
  public static final String GWT_DISPATCHER_SERVLET_MAPPING = "/application/" + BASE_GWT_PATH + "/*";

  /**
   * URL pattern for JSON dispatcher servlets.
   */
  public static final String JSON_DISPATCHER_SERVLET_MAPPING = "/application/" + BASE_JSON_PATH + "/*";

  /**
   * Builder service path, relative to the base service path.
   */
  public static final String BUILDER_RELATIVE_PATH = "/builder";

  /**
   * Builder service path.
   */
  public static final String BUILDER_GWT_PATH = BASE_GWT_PATH + BUILDER_RELATIVE_PATH;

  /**
   * Builder service path.
   */
  public static final String BUILDER_JSON_PATH = BASE_JSON_PATH + BUILDER_RELATIVE_PATH;

}
