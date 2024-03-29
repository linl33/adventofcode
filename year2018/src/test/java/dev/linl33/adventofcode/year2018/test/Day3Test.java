package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day3;

import java.util.Map;

class Day3Test implements AdventSolutionTest<Long, String> {
  @Override
  public AdventSolution<Long, String> newSolutionInstance() {
    return new Day3();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 110195L,
        "day3test", 4L
    );
  }

  @Override
  public Map<Object, String> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "#894",
        "day3test", "#3"
    );
  }
}
