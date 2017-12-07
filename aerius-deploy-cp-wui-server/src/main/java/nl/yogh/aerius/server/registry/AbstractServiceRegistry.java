package nl.yogh.aerius.server.registry;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * AbstractServiceRegistry implementation for default behavior.
 */
public class AbstractServiceRegistry implements ServiceRegistry {
  private final Map<String, Object> services = new HashMap<>();

  protected void addService(final String key, final Object value) {
    services.put(key, value);
  }

  @Override
  public Object findService(final HttpServletRequest request) {
    final String servicePath = request.getPathInfo();

    return services.get(servicePath);
  }
}
