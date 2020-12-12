package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day3;

import java.util.Map;

class Day3Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day3();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1983,
        "day3test1", 159,
        "day3test2", 135
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 107754,
        "day3test1", 610,
        "day3test2", 410
    );
  }
}
