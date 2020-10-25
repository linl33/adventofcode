package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface UncheckedConsumer<T> {
  void accept(T t) throws Exception;
}
