package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day17;

import java.util.Map;

class Day17Test implements AdventSolutionTest<Integer, Long> {
  @Override
  public AdventSolution<Integer, Long> newSolutionInstance() {
    return new Day17();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 4864
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 840248L
    );
  }
}
