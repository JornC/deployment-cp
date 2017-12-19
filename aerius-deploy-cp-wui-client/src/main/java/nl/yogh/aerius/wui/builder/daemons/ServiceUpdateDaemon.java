package nl.yogh.aerius.wui.builder.daemons;

import com.google.inject.ImplementedBy;

@ImplementedBy(ServiceUpdateDaemonImpl.class)
public interface ServiceUpdateDaemon {
  void start();

  void stop();
}
