package dev.linl33.adventofcode.year2019.intcodevm;

public interface MemoryAccessor<T> {
  T get();
  T set(T newVal);
  T add(T delta);

  default T set(MemoryAccessor<T> fromAccessor) {
    return set(fromAccessor.get());
  }
}
