package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;

import java.util.Map;

class Day5Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day5();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 832);
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 517);
  }
}
