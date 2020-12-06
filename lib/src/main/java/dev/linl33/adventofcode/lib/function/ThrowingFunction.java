package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface ThrowingFunction<T, R> {
  R apply(T t) throws Exception;

  default <V> ThrowingFunction<V, R> compose(ThrowingFunction<? super V, ? extends T> before) {
    return (V v) -> apply(before.apply(v));
  }

  default <V> ThrowingFunction<T, V> andThen(ThrowingFunction<? super R, ? extends V> after) {
    return (T t) -> after.apply(apply(t));
  }

  default ThrowingConsumer<T> andThenConsume(ThrowingConsumer<R> after) {
    return (T t) -> after.accept(apply(t));
  }

  static <T> ThrowingFunction<T, T> identity() {
    return t -> t;
  }
}
