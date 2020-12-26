package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;

import java.util.function.BiConsumer;

public enum SolutionPart {
  PART_1(
      AdventSolution::part1,
      AdventSolution::part1PrintMappingInternal,
      AdventSolution::getPart1Resource
  ),
  PART_2(
      AdventSolution::part2,
      AdventSolution::part2PrintMappingInternal,
      AdventSolution::getPart2Resource
  );

  public final SolutionFunction<?> part;
  public final BiConsumer<AdventSolution<?, ?>, ResourceIdentifier> printer;
  public final SupplierWithCtx<ResourceIdentifier> defaultResource;

  SolutionPart(ThrowingBiFunction<AdventSolution<?, ?>, ResourceIdentifier, ?> part,
               ThrowingBiFunction<AdventSolution<?, ?>, Object, ?> printMapping,
               SupplierWithCtx<ResourceIdentifier> defaultResource) {
    this.part = (adventSolution, s) -> SolutionUtil.adaptPartToRun(adventSolution, part, s);
    this.printer = (adventSolution, s) -> SolutionUtil.adaptPartToPrint(adventSolution, part, printMapping, s);
    this.defaultResource = defaultResource;
  }
}
