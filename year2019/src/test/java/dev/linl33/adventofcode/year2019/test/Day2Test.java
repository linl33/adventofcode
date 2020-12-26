package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day2;

import java.util.Map;

class Day2Test implements AdventSolutionTest<Long, Integer> {
  @Override
  public AdventSolution<Long, Integer> newSolutionInstance() {
    return new Day2();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 9706670L);
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 2552);
  }
}
