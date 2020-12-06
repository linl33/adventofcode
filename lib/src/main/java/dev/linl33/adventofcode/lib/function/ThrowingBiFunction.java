package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface ThrowingBiFunction<T, U, R> {
  R apply(T t, U u) throws Exception;

  default <V> ThrowingBiFunction<T, U, V> andThen(ThrowingFunction<? super R, ? extends V> after) throws Exception {
    return (T t, U u) -> after.apply(apply(t, u));
  }

  default ThrowingBiConsumer<T, U> andThenConsume(ThrowingConsumer<R> after) {
    return (T t, U u) -> after.accept(apply(t, u));
  }
}
