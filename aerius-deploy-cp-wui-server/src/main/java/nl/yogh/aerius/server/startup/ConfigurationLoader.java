package nl.yogh.aerius.server.startup;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.yogh.aerius.builder.domain.CompositionType;
import nl.yogh.aerius.builder.domain.ServiceType;
import nl.yogh.aerius.server.collections.MultiSet;
import nl.yogh.aerius.server.util.ApplicationConfiguration;

public class ConfigurationLoader {
  private static final Logger LOG = LoggerFactory.getLogger(ConfigurationLoader.class);

  private static final String CONFIG_PROPERTIES_NAME = "deploycp.properties";

  private static ApplicationConfiguration cachedCfg;

  public static Properties findProperties() {
    final Properties props = new Properties(System.getProperties());

    boolean success = false;
    while (!success) {
      try {
        final int propNum = props.size();

        final InputStream stream = ApplicationFactory.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_NAME);
        props.load(stream);

        success = true;
        LOG.info("Loaded configuration ({} variables) from file: {}", props.size() - propNum,
            ApplicationFactory.class.getClassLoader().getResource(CONFIG_PROPERTIES_NAME).getPath().toString());
      } catch (final IOException | NullPointerException e) {
        LOG.error("Could not load properties file from classpath (retrying in a while): {} ({})",
            CONFIG_PROPERTIES_NAME,
            System.getProperty("java.class.path"), e);

        try {
          // Sleep and retry
          Thread.sleep(15000);
        } catch (final InterruptedException e1) {
          throw new RuntimeException("Crapped out foh real.");
        }
      }
    }

    return props;
  }

  public static ApplicationConfiguration generate() {
    final ApplicationConfiguration cfg = new ApplicationConfiguration(findProperties());

    cfg.setCompositionTypes(discoverCompositions(cfg));
    cfg.setServiceTypes(discoverServiceTypes(cfg));

    for (final CompositionType compositionType : cfg.getCompositionTypes()) {
      for (final ServiceType serviceType : compositionType.serviceTypes()) {
        try {
          final Set<String> dirs = cfg.getServiceDirectories(serviceType);
          if (dirs.isEmpty()) {
            LOG.debug("Warning: service {} has no dependent directories configured.", serviceType.name());
          }
        } catch (final Exception e) {
          LOG.error(
              "ERROR: directory structure corrupt. Composition references services that do not exist. composition {} references service {}. This will result in errors.",
              compositionType.name(), serviceType.name());
        }
      }
    }

    ConfigurationLoader.cachedCfg = cfg;

    return cfg;
  }

  private static MultiSet<ServiceType, String> discoverServiceTypes(final ApplicationConfiguration cfg) {
    final List<ServiceType> services;
    try {
      services = Files.list(new File(String.format("%s/services/", cfg.getStagingDir())).toPath())
          .filter(Files::isDirectory)
          .map(v -> v.getFileName().toString())
          .map(v -> new ServiceType(v))
          .collect(Collectors.toList());
    } catch (final IOException e) {
      LOG.error("Error while discovering compositions for {}", cfg.getStagingDir(), e);
      throw new RuntimeException("Corrupted directory structure.");
    }

    final MultiSet<ServiceType, String> serviceTypes = new MultiSet<>();
    final Function<ServiceType, Set<String>> discoverService = discoverServiceDirectories(cfg);
    services.stream().forEach(v -> serviceTypes.get(v).addAll(discoverService.apply(v)));

    return serviceTypes;
  }

  private static Function<ServiceType, Set<String>> discoverServiceDirectories(final ApplicationConfiguration cfg) {
    return v -> {
      try {
        return Files.lines(new File(String.format("%s/services/%s/.directories", cfg.getStagingDir(), v.name())).toPath())
            .collect(Collectors.toSet());
      } catch (final IOException e) {
        LOG.error("Error while finding directories for service {}", v, e);
        return new HashSet<>();
      }
    };
  }

  private static Set<CompositionType> discoverCompositions(final ApplicationConfiguration cfg) {
    List<String> compositions;
    try {
      compositions = Files.list(new File(String.format("%s/compositions/", cfg.getStagingDir())).toPath())
          .filter(Files::isDirectory)
          .map(f -> f.getFileName().toString())
          .collect(Collectors.toList());
    } catch (final IOException e) {
      LOG.error("Error while discovering compositions for {}", cfg.getStagingDir(), e);
      compositions = new ArrayList<>();
    }

    return compositions.stream().map(discoverComposition(cfg)).collect(Collectors.toSet());
  }

  private static Function<String, CompositionType> discoverComposition(final ApplicationConfiguration cfg) {
    return v -> {
      try {
        return new CompositionType(v,
            // Ideally we wouldn't have a type conversion here, but I have no idea how to return a specifically typed collection with .collect
            new HashSet<>(
                Files.lines(new File(String.format("%s/compositions/%s/.services", cfg.getStagingDir(), v)).toPath())
                    .map(s -> new ServiceType(s))
                    .collect(Collectors.toSet())));
      } catch (final IOException e) {
        LOG.error("Error while generating composition for {}", v, e);
        throw new RuntimeException("Corrupted directory structure.");
      }
    };
  }

  public static ApplicationConfiguration getCachedInstance() {
    return cachedCfg;
  }
}
