package dev.linl33.adventofcode.year2021.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2021.Day9;

import java.util.Map;

class Day9Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day9();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 545,
        "day9test1", 15
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 950600,
        "day9test1", 1134
    );
  }
}
