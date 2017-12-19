package nl.yogh.aerius.server.collections;

import java.util.ArrayList;
import java.util.List;

public class MultiMap<K, V> extends AutoFillingTreeMap<K, List<V>> {
  private static final long serialVersionUID = 7987334298722116268L;

  @Override
  protected List<V> createEmptyRecord(final K key) {
    return new ArrayList<>();
  }
}
