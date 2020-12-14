package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day14;

import java.util.Map;

class Day14Test implements AdventSolutionTest<Long, Long> {

  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day14();
  }

  @Override
  public Map<String, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 11884151942312L,
        "day14test1", 165L
    );
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 2625449018811L,
        "day14test2", 208L
    );
  }
}
