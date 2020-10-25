package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface UncheckedFunction<T, R> {
  R apply(T t) throws Exception;

  default <V> UncheckedFunction<V, R> compose(UncheckedFunction<? super V, ? extends T> before) {
    return (V v) -> apply(before.apply(v));
  }

  default <V> UncheckedFunction<T, V> andThen(UncheckedFunction<? super R, ? extends V> after) {
    return (T t) -> after.apply(apply(t));
  }

  default UncheckedConsumer<T> andThenConsume(UncheckedConsumer<R> after) {
    return (T t) -> after.accept(apply(t));
  }

  static <T> UncheckedFunction<T, T> identity() {
    return t -> t;
  }
}
