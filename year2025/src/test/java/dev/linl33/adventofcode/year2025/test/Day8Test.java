package dev.linl33.adventofcode.year2025.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2025.Day8;

import java.util.Map;

public class Day8Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day8();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
      newSolutionInstance().getPart1Resource(), 72150L
      // TODO: needs a different parameter
      // "day8test1", 21L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
      newSolutionInstance().getPart2Resource(), 3926518899L,
      "day8test1", 25272L
    );
  }
}
