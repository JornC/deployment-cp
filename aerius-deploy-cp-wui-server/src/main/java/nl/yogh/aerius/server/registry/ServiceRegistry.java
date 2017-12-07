package nl.yogh.aerius.server.registry;

import javax.servlet.http.HttpServletRequest;

/**
 * A service registry is responsible for managing services and mapping incoming http requests to services.
 */
public interface ServiceRegistry {

  /**
   * Finds the service to handle the request.
   *
   * @param request
   *          The http request to find a service for.
   * @return The service to handle the request, or null if none was found
   */
  Object findService(HttpServletRequest request);
}