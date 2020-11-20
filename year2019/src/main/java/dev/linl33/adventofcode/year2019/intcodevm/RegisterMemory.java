//package dev.linl33.adventofcode.year2019.intcodevm;
//
//import dev.linl33.adventofcode.year2019.intcodevm.register.IRegister;
//
//import java.util.Collections;
//import java.util.Map;
//
//public class RegisterMemory<K extends Class<? extends IRegister<T>>, V extends MemoryAccessor<T>, T> implements IntcodeMemory<V, K> {
//  private final Map<K, V> backingMap;
//
//  public RegisterMemory(Map<K, V> backingMap) {
//    this.backingMap = Collections.unmodifiableMap(backingMap);
//  }
//
//  @Override
//  public V read(K k) {
//    return backingMap.get(k);
//  }
//
//  @Override
//  public V write(V v, K k) {
//    throw new UnsupportedOperationException();
//  }
//
//  @Override
//  public String print() {
//    return backingMap.toString();
//  }
//}
