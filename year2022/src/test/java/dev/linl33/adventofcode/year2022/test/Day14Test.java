package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day14;

import java.util.Map;

public class Day14Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day14();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        "day14", 892,
        "day14test1", 24
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        "day14", 27155,
        "day14test1", 93
    );
  }
}
