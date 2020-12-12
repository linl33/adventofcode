package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day6;

import java.util.Map;

class Day6Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day6();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 402879,
        "day6test1", 42
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 484,
        "day6test2", 4
    );
  }
}
