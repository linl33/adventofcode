package dev.linl33.adventofcode.year2019.intcodevm.register;

import dev.linl33.adventofcode.year2019.intcodevm.MemoryAccessor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class RegisterMap<K extends Class<? extends IRegister<T>>, V extends MemoryAccessor<T>, T> implements Map<K, V> {
  private final Map<K, V> backingMap;

  public RegisterMap(Map<K, V> backingMap) {
    this.backingMap = Collections.unmodifiableMap(backingMap);
  }

  @Override
  public int size() {
    return backingMap.size();
  }

  @Override
  public boolean isEmpty() {
    return backingMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return backingMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return backingMap.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return backingMap.get(key);
  }

  @Override
  public V put(K key, V value) {
    return backingMap.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return backingMap.remove(key);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    backingMap.putAll(m);
  }

  @Override
  public void clear() {
    backingMap.clear();
  }

  @Override
  public Set<K> keySet() {
    return backingMap.keySet();
  }

  @Override
  public Collection<V> values() {
    return backingMap.values();
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return backingMap.entrySet();
  }
}
