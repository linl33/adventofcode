package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface UncheckedBiConsumer<T, U> {
  void accept(T t, U u) throws Exception;

  default UncheckedBiConsumer<T, U> andThen(UncheckedBiConsumer<? super T, ? super U> after) {
    return (l, r) -> {
      accept(l, r);
      after.accept(l, r);
    };
  }
}
