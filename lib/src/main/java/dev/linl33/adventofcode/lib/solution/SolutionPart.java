package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.UncheckedBiFunction;

import java.io.BufferedReader;
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
  public final BiConsumer<AdventSolution<?, ?>, String> printer;
  public final SupplierWithCtx<String> defaultResource;

  SolutionPart(UncheckedBiFunction<AdventSolution<?, ?>, BufferedReader, ?> part,
               UncheckedBiFunction<AdventSolution<?, ?>, Object, ?> printMapping,
               SupplierWithCtx<String> defaultResource) {
    this.part = (adventSolution, s) -> SolutionUtil.adaptPartToRun(adventSolution, part, s);
    this.printer = (adventSolution, s) -> SolutionUtil.adaptPartToPrint(adventSolution, part, printMapping, s);
    this.defaultResource = defaultResource;
  }
}
