package nl.yogh.aerius.server.startup;

import java.util.ArrayList;
import java.util.stream.Collectors;

import nl.yogh.aerius.server.collections.MultiMap;

public class TimestampedMultiMap<V> extends MultiMap<Long, V> {
  private static final long serialVersionUID = -6510616941939238749L;

  public TimestampedMultiMap() {}

  public ArrayList<V> getUpdates(final long since) {
    return entrySet().stream().filter(e -> e.getKey() > since).flatMap(e -> e.getValue().stream()).collect(Collectors.toCollection(ArrayList::new));
  }
}
