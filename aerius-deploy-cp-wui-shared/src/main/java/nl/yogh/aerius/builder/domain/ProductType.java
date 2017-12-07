package nl.yogh.aerius.builder.domain;

public enum ProductType {
  CALCULATOR(ServiceType.CALCULATOR_WUI, ServiceType.CALCULATOR_DATABASE, ServiceType.CALCULATOR_GEOSERVER),

  REGISTER(ServiceType.REGISTER_WUI, ServiceType.REGISTER_DATABASE, ServiceType.REGISTER_GEOSERVER),

  SCENARIO(ServiceType.SCENARIO_WUI, ServiceType.SCENARIO_DATABASE, ServiceType.SCENARIO_GEOSERVER),

  WORKERS(ServiceType.TASKMANAGER, ServiceType.WORKER_OPS, ServiceType.WORKER_SRM, ServiceType.WORKER_CALCULATE, ServiceType.WORKER_IMPORT),

  CONNECT(ServiceType.CONNECT, ServiceType.CONNECT_DATABASE),

  MELDING(ServiceType.MELDING),

  TESTS(ServiceType.SELENIUM);

  private final ServiceType[] serviceTypes;

  private ProductType(final ServiceType... serviceTypes) {
    this.serviceTypes = serviceTypes;
  }

  public ServiceType[] getServiceTypes() {
    return serviceTypes;
  }
}
