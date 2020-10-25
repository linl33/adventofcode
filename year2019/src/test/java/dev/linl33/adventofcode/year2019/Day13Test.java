package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;

import java.util.Map;

class Day13Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day13();
  }

  @Override
  public Map<String, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 344L);
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 17336L);
  }
}
