package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day12;

import java.util.Map;

class Day12Test implements AdventSolutionTest<Integer, Long> {
  @Override
  public AdventSolution<Integer, Long> newSolutionInstance() {
    return new Day12();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 4110);
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 2650000000466L);
  }
}
