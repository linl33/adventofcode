package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;

import java.util.Map;

class Day10Test implements AdventSolutionTest<Integer, Long> {
  @Override
  public AdventSolution<Integer, Long> newSolutionInstance() {
    return new Day10();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 2240,
        "day10test1", 35,
        "day10test2", 220
    );
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 99214346656768L,
        "day10test1", 8L,
        "day10test2", 19208L
    );
  }
}
