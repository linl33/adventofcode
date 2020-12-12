package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day3;

import java.util.Map;

class Day3Test implements AdventSolutionTest<Integer, Long> {
  @Override
  public AdventSolution<Integer, Long> newSolutionInstance() {
    return new Day3();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 162,
        "day3test1", 7
    );
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 3064612320L,
        "day3test1", 336L
    );
  }
}
