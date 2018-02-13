package nl.yogh.aerius.wui.builder.daemons;

import com.google.inject.ImplementedBy;

@ImplementedBy(CompositionUpdateDaemonImpl.class)
public interface CompositionUpdateDaemon {
  void start();

  void stop();
}
