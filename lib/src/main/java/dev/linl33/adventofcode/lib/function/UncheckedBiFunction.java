package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface UncheckedBiFunction<T, U, R> {
  R apply(T t, U u) throws Exception;

  default <V> UncheckedBiFunction<T, U, V> andThen(UncheckedFunction<? super R, ? extends V> after) throws Exception {
    return (T t, U u) -> after.apply(apply(t, u));
  }

  default UncheckedBiConsumer<T, U> andThenConsume(UncheckedConsumer<R> after) {
    return (T t, U u) -> after.accept(apply(t, u));
  }
}
