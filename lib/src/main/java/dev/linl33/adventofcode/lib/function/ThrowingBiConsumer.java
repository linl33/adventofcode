package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U> {
  void accept(T t, U u) throws Exception;

  default ThrowingBiConsumer<T, U> andThen(ThrowingBiConsumer<? super T, ? super U> after) {
    return (l, r) -> {
      accept(l, r);
      after.accept(l, r);
    };
  }
}
