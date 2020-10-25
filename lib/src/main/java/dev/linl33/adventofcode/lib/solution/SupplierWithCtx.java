package dev.linl33.adventofcode.lib.solution;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface SupplierWithCtx<T> extends Function<AdventSolution<?, ?>, T> {
  default Consumer<AdventSolution<?, ?>> andThenConsume(BiConsumer<AdventSolution<?, ?>, T> after) {
    return adventSolution -> after.accept(adventSolution, apply(adventSolution));
  }
}
