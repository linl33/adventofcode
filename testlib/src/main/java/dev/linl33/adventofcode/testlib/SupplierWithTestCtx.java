package dev.linl33.adventofcode.testlib;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface SupplierWithTestCtx<T> extends Function<AdventSolutionTest<?, ?>, T> {
  default Consumer<AdventSolutionTest<?, ?>> andThenConsume(BiConsumer<AdventSolutionTest<?, ?>, T> after) {
    return adventSolution -> after.accept(adventSolution, apply(adventSolution));
  }
}
