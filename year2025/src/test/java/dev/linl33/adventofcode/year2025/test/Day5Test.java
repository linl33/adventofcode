package dev.linl33.adventofcode.year2025.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2025.Day5;

import java.util.Map;

public class Day5Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day5();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
      newSolutionInstance().getPart1Resource(), 661L,
      "day5test1", 3L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
      newSolutionInstance().getPart2Resource(), 359526404143208L,
      "day5test1", 14L
    );
  }
}
