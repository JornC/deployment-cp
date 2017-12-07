package nl.yogh.aerius.server.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.server.registry.ServiceRegistry;

public abstract class JSONDispatcherServlet extends HttpServlet {
  private static final long serialVersionUID = -1301723695138457892L;

  private static final Logger LOG = LoggerFactory.getLogger(JSONDispatcherServlet.class);

  protected ServiceRegistry registry;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);

    LOG.info("Initializing dispatcher " + getClass().getSimpleName() + " on " + config.getServletName());
  }

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse resp) throws ServletException, IOException {
    final Object service = registry.findService(request);
    if (service == null) {
      LOG.error("No service for path: {}", request.getPathInfo());
    }

    final String methodName = request.getParameter("method");

    final Class<?> c = service.getClass();

    LOG.info(service.getClass()
        .getSimpleName() + " > " + methodName);

    try {
      final Method method = c.getDeclaredMethod(methodName);
      final Object returnObject = method.invoke(service, new Object[] {});
      final ObjectMapper mapper = new ObjectMapper();
      mapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
      mapper.writeValue(resp.getOutputStream(), returnObject);
      resp.getOutputStream()
      .close();
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      LOG.error("Could not finish method: {}", methodName, e);
    }
  }
}
