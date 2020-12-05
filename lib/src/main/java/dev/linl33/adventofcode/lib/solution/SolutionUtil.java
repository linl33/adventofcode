package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.UncheckedBiFunction;
import dev.linl33.adventofcode.lib.util.PrintUtil;

import java.io.BufferedReader;

public class SolutionUtil {
  static Object adaptPartToRun(AdventSolution<?, ?> solution,
                               UncheckedBiFunction<AdventSolution<?, ?>, BufferedReader, ?> solutionMethod,
                               String inputResource) {
    try (var reader = solution.resourceSupplier(inputResource)) {
      return solutionMethod.apply(solution, reader);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static void adaptPartToPrint(AdventSolution<?, ?> solution,
                               UncheckedBiFunction<AdventSolution<?, ?>, BufferedReader, ?> solutionMethod,
                               UncheckedBiFunction<AdventSolution<?, ?>, Object, ?> printMapping,
                               String inputResource) {
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

  @SuppressWarnings("unchecked")
  public static <T1, T2, T extends AdventSolution<T1, T2>, TOut> TOut adaptPartToRunGeneric(T solution,
                                                                                            UncheckedBiFunction<T, BufferedReader, TOut> solutionMethod,
                                                                                            String inputResource) {
    return (TOut) adaptPartToRun(
        solution,
        (UncheckedBiFunction<AdventSolution<?, ?>, BufferedReader, ?>) solutionMethod,
        inputResource
    );
  }

  @SuppressWarnings("unchecked")
  public static <T1, T2, T extends AdventSolution<T1, T2>, TOut> void adaptPartToPrintGeneric(T solution,
                                                                                              UncheckedBiFunction<T, BufferedReader, TOut> solutionMethod,
                                                                                              UncheckedBiFunction<T, TOut, ?> printMapping,
                                                                                              String inputResource) {
    adaptPartToPrint(
        solution,
        (UncheckedBiFunction<AdventSolution<?, ?>, BufferedReader, ?>) solutionMethod,
        (UncheckedBiFunction<AdventSolution<?, ?>, Object, ?>) printMapping,
        inputResource
    );
  }
}
