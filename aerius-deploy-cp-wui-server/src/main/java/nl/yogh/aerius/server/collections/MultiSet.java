package nl.yogh.aerius.server.collections;

import java.util.HashSet;
import java.util.Set;

public class MultiSet<K, V> extends AutoFillingDictionary<K, Set<V>> {
  private static final long serialVersionUID = -1631510699166475507L;

  @Override
  protected Set<V> createEmptyRecord(final K key) {
    return new HashSet<>();
  }
}
