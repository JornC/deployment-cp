package nl.yogh.aerius.server.util;

import nl.yogh.aerius.builder.domain.ServiceType;

public class ProjectTypeDirectoryUtil {
  public static String[] getServiceDirectories(final ServiceType type) {
    final String[] str = new String[0];

/*
    switch (type) {
    case CALCULATOR_DATABASE:
      str = new String[] { "./aerius-database-common", "./aerius-database-calculator" };
      break;
    case CALCULATOR_WUI:
      str = new String[] { "./aerius-taskmanager-client", "./aerius-wui-client", "./aerius-wui-scenario-base-client",
          "./aerius-wui-calculator-client", "./aerius-wui-server", "./aerius-wui-calculator-server" };
      break;
    case SCENARIO_BASE_GEOSERVER:
      str = new String[] { "./aerius-geoserver-base", "./aerius-geoserver-calculator" };
      break;
    case SCENARIO_DATABASE:
      str = new String[] { "./aerius-database-common", "./aerius-database-calculator" };
      break;
    case SCENARIO_WUI:
      str = new String[] { "./aerius-taskmanager-client", "./aerius-wui-client", "./aerius-wui-scenario-base-client", "./aerius-wui-scenario-client",
          "./aerius-wui-server", "./aerius-wui-scenario-server" };
      break;
    case REGISTER_DATABASE:
      str = new String[] { "./aerius-database-common", "./aerius-database-register" };
      break;
    case REGISTER_GEOSERVER:
      str = new String[] { "./aerius-geoserver-base", "./aerius-geoserver-register" };
      break;
    case REGISTER_WUI:
      str = new String[] { "./aerius-taskmanager-client", "./aerius-wui-client", "./aerius-wui-register-client", "./aerius-wui-server",
      "./aerius-wui-register-server" };
      break;
    case MONITOR_DATABASE:
      str = new String[] { "./aerius-database-common", "./aerius-database-monitor" };
      break;
    case CONNECT:
      str = new String[] { "./aerius-connect-server", "./aerius-connect-model" };
      break;
    case TASKMANAGER:
      str = new String[] { "./aerius-taskmanager" };
      break;
    case WORKER_OPS:
      str = new String[] { "./aerius-tasks", "./aerius-worker-ops" };
      break;
    case WORKER_SRM:
      str = new String[] { "./aerius-tasks", "./aerius-worker-srm" };
      break;
    case MELDING:
      str = new String[] { "./aerius-wui-melding-client" };
      break;
    default:
      str = null;
    }
    */

    return str;
  }
}
