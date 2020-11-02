package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;

import java.util.Map;

class Day19Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day19();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 173);
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 6671097);
  }
}
