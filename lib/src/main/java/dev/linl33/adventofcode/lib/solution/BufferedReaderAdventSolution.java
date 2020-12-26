package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;

import java.io.BufferedReader;

public interface BufferedReaderAdventSolution<T1, T2> extends AdventSolution<T1, T2>, ResourceServiceHolder {
  @Override
  default T1 part1(ResourceIdentifier identifier) throws Exception {
    return SolutionUtil.runWithResource(identifier, getResourceService()::asBufferedReader, this::part1);
  }

  @Override
  default T2 part2(ResourceIdentifier identifier) throws Exception {
    return SolutionUtil.runWithResource(identifier, getResourceService()::asBufferedReader, this::part2);
  }

  @Override
  default <U, A extends AdventSolution<T1, T2>> U run(ThrowingBiFunction<A, ?, U> customPart, ResourceIdentifier resource) {
    return (U) SolutionUtil.adaptPartToRun(
        this,
        convert((ThrowingBiFunction<BufferedReaderAdventSolution<T1, T2>, BufferedReader, ?>) customPart),
        resource
    );
  }

  @Override
  default <U, A extends AdventSolution<T1, T2>> void print(ThrowingBiFunction<A, ?, U> customPart,
                                                           ThrowingBiFunction<A, U, ?> printMapping,
                                                           ResourceIdentifier resource) {
    SolutionUtil.adaptPartToPrint(
        this,
        convert((ThrowingBiFunction<BufferedReaderAdventSolution<T1, T2>, BufferedReader, ?>) customPart),
        (ThrowingBiFunction<BufferedReaderAdventSolution<T1, T2>, Object, ?>) printMapping,
        resource
    );
  }

  T1 part1(BufferedReader reader) throws Exception;

  T2 part2(BufferedReader reader) throws Exception;

  private <A extends BufferedReaderAdventSolution<T1, T2>, T> ThrowingBiFunction<A, ResourceIdentifier, T> convert(ThrowingBiFunction<A, BufferedReader, T> orig) {
    return (instance, identifier) -> SolutionUtil.runWithResource(
        instance,
        identifier,
        (a, resId) -> a.getResourceService().asBufferedReader(resId),
        orig
    );
  }
}
