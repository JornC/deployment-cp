package nl.yogh.aerius.server.service;

import java.util.ArrayList;

import nl.yogh.aerius.builder.domain.CompositionType;
import nl.yogh.aerius.builder.exception.ApplicationException;
import nl.yogh.aerius.builder.service.ApplicationService;
import nl.yogh.aerius.server.util.ApplicationConfiguration;

public class ApplicationServiceImpl implements ApplicationService {
  private final ApplicationConfiguration cfg;

  public ApplicationServiceImpl(final ApplicationConfiguration cfg) {
    this.cfg = cfg;
  }

  @Override
  public ArrayList<CompositionType> getApplicationConfig() throws ApplicationException {
    return new ArrayList<>(cfg.getCompositionTypes());
  }
}
