package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day17;

import java.util.Map;

class Day17Test implements AdventSolutionTest<Integer, Integer> {

  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day17();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 209,
        "day17test1", 112
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1492,
        "day17test1", 848
    );
  }
}
