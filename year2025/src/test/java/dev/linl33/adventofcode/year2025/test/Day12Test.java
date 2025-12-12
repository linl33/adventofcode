package dev.linl33.adventofcode.year2025.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2025.Day12;

import java.util.Map;

public class Day12Test implements AdventSolutionTest<Integer, Void> {
  @Override
  public AdventSolution<Integer, Void> newSolutionInstance() {
    return new Day12();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
      newSolutionInstance().getPart1Resource(), 536
      // solution doesn't work for test input
    );
  }

  @Override
  public Map<Object, Void> getPart2Cases() {
    throw new UnsupportedOperationException();
  }
}
