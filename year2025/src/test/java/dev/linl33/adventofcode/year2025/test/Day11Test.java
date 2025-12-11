package dev.linl33.adventofcode.year2025.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2025.Day11;

import java.util.Map;

public class Day11Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day11();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
      newSolutionInstance().getPart1Resource(), 466L,
       "day11test1", 5L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
      newSolutionInstance().getPart2Resource(), 549705036748518L,
      "day11test2", 2L
    );
  }
}
