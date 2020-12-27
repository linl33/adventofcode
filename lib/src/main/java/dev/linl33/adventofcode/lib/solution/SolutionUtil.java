package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.function.ThrowingFunction;
import dev.linl33.adventofcode.lib.util.PrintUtil;
import org.jetbrains.annotations.NotNull;

public class SolutionUtil {
  static <A> Object adaptPartToRun(A solution,
                                   ThrowingBiFunction<A, ResourceIdentifier, ?> solutionMethod,
                                   ResourceIdentifier inputResource) {
    try {
      return solutionMethod.apply(solution, inputResource);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static <A> void adaptPartToPrint(A solution,
                                   ThrowingBiFunction<A, ResourceIdentifier, ?> solutionMethod,
                                   ThrowingBiFunction<A, Object, ?> printMapping,
                                   ResourceIdentifier inputResource) {
    try {
      printMapping
          .andThenConsume(PrintUtil::enhancedPrint)
          .accept(solution, adaptPartToRun(solution, solutionMethod, inputResource));
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static <T, R extends AutoCloseable> T runWithResource(@NotNull ResourceIdentifier identifier,
                                                        @NotNull ThrowingFunction<ResourceIdentifier, R> resourceFunc,
                                                        @NotNull ThrowingFunction<R, T> partFunc) throws Exception {
    try (var res = resourceFunc.apply(identifier)) {
      return partFunc.apply(res);
    }
  }

  static <T, R extends AutoCloseable, A> T runWithResource(@NotNull A instance,
                                                           @NotNull ResourceIdentifier identifier,
                                                           @NotNull ThrowingBiFunction<A, ResourceIdentifier, R> resourceFunc,
                                                           @NotNull ThrowingBiFunction<A, R, T> partFunc) throws Exception {
    try (var res = resourceFunc.apply(instance, identifier)) {
      return partFunc.apply(instance, res);
    }
  }
}
