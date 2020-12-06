package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface ThrowingConsumer<T> {
  void accept(T t) throws Exception;
}
