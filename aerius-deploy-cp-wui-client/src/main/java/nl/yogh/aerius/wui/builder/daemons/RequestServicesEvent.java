package nl.yogh.aerius.wui.builder.daemons;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import nl.yogh.aerius.builder.domain.ServiceInfo;

public class RequestServicesEvent extends ResponseEvent<ArrayList<ServiceInfo>, List<ServiceInfo>> {
  public RequestServicesEvent(final ArrayList<ServiceInfo> request, final Consumer<List<ServiceInfo>> response) {
    super(request, response);
  }
}
