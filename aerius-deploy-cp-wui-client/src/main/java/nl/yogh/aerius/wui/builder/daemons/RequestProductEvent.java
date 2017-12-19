package nl.yogh.aerius.wui.builder.daemons;

import java.util.function.Consumer;

import nl.yogh.aerius.builder.domain.ProjectInfo;

public class RequestProductEvent extends ResponseEvent<String, ProjectInfo> {
  public RequestProductEvent(final String request, final Consumer<ProjectInfo> consumer) {
    super(request, consumer);
  }
}
