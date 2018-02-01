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
   * Module name
   */
  public static final String MODULE_NAME = "/application/";

  /**
   * URL pattern for service dispatcher servlets.
   */
  public static final String GWT_DISPATCHER_SERVLET_MAPPING = MODULE_NAME + BASE_GWT_PATH + "/*";

  /**
   * URL pattern for JSON dispatcher servlets.
   */
  public static final String JSON_DISPATCHER_SERVLET_MAPPING = MODULE_NAME + BASE_JSON_PATH + "/*";

  /**
   * Docker service path, relative to the base service path.
   */
  public static final String DOCKER_RELATIVE_PATH = "/dockers";

  /**
   * Docker service path.
   */
  public static final String DOCKER_GWT_PATH = BASE_GWT_PATH + DOCKER_RELATIVE_PATH;

  /**
   * Docker service path.
   */
  public static final String DOCKER_JSON_PATH = BASE_JSON_PATH + DOCKER_RELATIVE_PATH;

  /**
   * Pull request service path, relative to the base service path.
   */
  public static final String PULL_REQUEST_RELATIVE_PATH = "/pull";

  /**
   * Pull request service path.
   */
  public static final String PULL_REQUEST_GWT_PATH = BASE_GWT_PATH + PULL_REQUEST_RELATIVE_PATH;

  /**
   * Pull request service path.
   */
  public static final String PULL_REQUEST_JSON_PATH = BASE_JSON_PATH + PULL_REQUEST_RELATIVE_PATH;

  /**
   * Log service path, relative to the base service path.
   */
  public static final String LOG_RELATIVE_PATH = "/log";

  /**
   * Log request service path.
   */
  public static final String LOG_GWT_PATH = BASE_GWT_PATH + LOG_RELATIVE_PATH;

  /**
   * Log request service path.
   */
  public static final String LOG_JSON_PATH = BASE_JSON_PATH + LOG_RELATIVE_PATH;

}
