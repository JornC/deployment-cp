package nl.yogh.aerius.wui.builder.daemons;

import com.google.inject.ImplementedBy;

@ImplementedBy(ProjectUpdateDaemonImpl.class)
public interface ProjectUpdateDaemon {
  void start();

  void stop();
}
