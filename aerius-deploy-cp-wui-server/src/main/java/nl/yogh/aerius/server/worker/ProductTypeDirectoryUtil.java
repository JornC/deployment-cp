package nl.yogh.aerius.server.worker;

import nl.yogh.aerius.builder.domain.ProductType;
import nl.yogh.aerius.builder.domain.ServiceType;

public class ProductTypeDirectoryUtil {
  public static String getServiceDirectories(final ServiceType type) {
    final String str;

    switch (type) {
    case CALCULATOR_DATABASE:
      str = "./aerius-database-common ./aerius-database-calculator";
      break;
    case CALCULATOR_WUI:
      str = "./aerius-taskmanager-client ./aerius-wui-client ./aerius-wui-scenario-base-client ./aerius-wui-calculator-client ./aerius-wui-server ./aerius-wui-calculator-server";
      break;
    case SCENARIO_BASE_GEOSERVER:
      str = "./aerius-geoserver-base ./aerius-geoserver-calculator";
      break;
    case SCENARIO_DATABASE:
      str = "./aerius-database-common ./aerius-database-calculator";
      break;
    case SCENARIO_WUI:
      str = "./aerius-taskmanager-client ./aerius-wui-client ./aerius-wui-scenario-base-client ./aerius-wui-scenario-client ./aerius-wui-server ./aerius-wui-scenario-server";
      break;
    case REGISTER_DATABASE:
      str = "./aerius-database-common ./aerius-database-register";
      break;
    case REGISTER_GEOSERVER:
      str = "./aerius-geoserver-base ./aerius-geoserver-register";
      break;
    case REGISTER_WUI:
      str = "./aerius-taskmanager-client ./aerius-wui-client ./aerius-wui-register-client ./aerius-wui-server ./aerius-wui-register-server";
      break;
    case MONITOR_DATABASE:
      str = "./aerius-database-common ./aerius-database-monitor";
      break;
    case  CONNECT:
      str = "./aerius-connect-server ./aerius-connect-model";
      break;
    case TASKMANAGER:
      str = "./aerius-taskmanager";
      break;
    case WORKER_OPS:
      str = "./aerius-tasks ./aerius-worker-ops";
      break;
    case WORKER_SRM:
      str = "./aerius-tasks ./aerius-worker-srm";
      break;
    case MELDING:
      str = "./aerius-wui-melding-client";
      break;
    default:
      str = null;
    }

    return str;
  }

  public static String getProductDirectories(final ProductType type) {
    final ServiceType[] serviceTypes = type.getServiceTypes();
    final String[] directories = new String[serviceTypes.length];

    for (int i = 0; i < serviceTypes.length; i++) {
      final String serviceDirectories = getServiceDirectories(serviceTypes[i]);
      if (serviceDirectories == null) {
        return null;
      }

      directories[i] = serviceDirectories;
    }

    return String.join(" ", directories);
  }
}
