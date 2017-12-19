package nl.yogh.aerius.wui.builder.daemons;

import com.google.inject.ImplementedBy;

@ImplementedBy(ProductUpdateDaemonImpl.class)
public interface ProductUpdateDaemon {
  void start();

  void stop();
}
