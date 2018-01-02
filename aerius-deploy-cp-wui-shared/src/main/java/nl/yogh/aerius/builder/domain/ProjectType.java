package nl.yogh.aerius.builder.domain;

public enum ProjectType {
  CALCULATOR(ServiceType.CALCULATOR_WUI, ServiceType.CALCULATOR_DATABASE, ServiceType.SCENARIO_BASE_GEOSERVER, ServiceType.TASKMANAGER, ServiceType.WORKER_OPS, ServiceType.WORKER_SRM),

  SCENARIO(ServiceType.SCENARIO_WUI, ServiceType.SCENARIO_DATABASE, ServiceType.SCENARIO_BASE_GEOSERVER, ServiceType.TASKMANAGER, ServiceType.WORKER_OPS, ServiceType.WORKER_SRM),

  REGISTER(ServiceType.REGISTER_WUI, ServiceType.REGISTER_DATABASE, ServiceType.REGISTER_GEOSERVER),

  CONNECT(ServiceType.CONNECT),

  MELDING(ServiceType.MELDING);

  private final ServiceType[] serviceTypes;

  private ProjectType(final ServiceType... serviceTypes) {
    this.serviceTypes = serviceTypes;
  }

  public ServiceType[] getServiceTypes() {
    return serviceTypes;
  }
}
