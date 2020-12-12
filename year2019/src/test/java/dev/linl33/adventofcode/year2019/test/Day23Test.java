package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day23;

import java.util.Map;

class Day23Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day23();
  }

  @Override
  public Map<String, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 17949L);
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 12326L);
  }
}
