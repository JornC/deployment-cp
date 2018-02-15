package nl.yogh.aerius.server.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import nl.yogh.aerius.builder.service.ServiceURLConstants;
import nl.yogh.aerius.server.registry.BuilderServiceRegistry;

@WebServlet(ServiceURLConstants.JSON_DISPATCHER_SERVLET_MAPPING)
public class JSONBuilderDispatcherServlet extends JSONDispatcherServlet {
  private static final long serialVersionUID = -6207400049221171875L;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);

    registry = new BuilderServiceRegistry();
  }
}
