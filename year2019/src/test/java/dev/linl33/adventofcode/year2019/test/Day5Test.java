package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day5;

import java.util.Map;

class Day5Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day5();
  }

  @Override
  public Map<String, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 12428642L);
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 918655L);
  }
}
