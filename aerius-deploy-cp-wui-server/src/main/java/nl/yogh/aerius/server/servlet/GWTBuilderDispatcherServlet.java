package nl.yogh.aerius.server.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import nl.yogh.aerius.builder.service.ServiceURLConstants;
import nl.yogh.aerius.server.registry.BuilderServiceRegistry;

/**
 * Dispatcher servlet for Scenario. Uses a ScenarioServiceRegistry instance for service lookups and provides it with a ScenarioSession.
 */
@WebServlet(ServiceURLConstants.GWT_DISPATCHER_SERVLET_MAPPING)
public class GWTBuilderDispatcherServlet extends GWTDispatcherServlet {
  private static final long serialVersionUID = -6575847931276075565L;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);

    registry = new BuilderServiceRegistry();
  }
}
