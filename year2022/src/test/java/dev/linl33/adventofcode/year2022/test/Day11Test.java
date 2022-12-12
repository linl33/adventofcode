package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day11;

import java.util.Map;

public class Day11Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day11();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        "day11", 50172L,
        "day11test1", 10605L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        "day11", 11614682178L,
        "day11test1", 2713310158L
    );
  }
}
