package dev.linl33.adventofcode.year2023.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2023.Day10;

import java.util.Map;

public class Day10Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day10();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
      newSolutionInstance().getPart1Resource(), 6860,
      "day10test1", 4,
      "day10test2", 4,
      "day10test3", 8,
      "day10test4", 8,
      "day10test5", 23,
      "day10test6", 70,
      "day10test7", 80
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
      newSolutionInstance().getPart1Resource(), 343,
      "day10test1", 1,
      "day10test2", 1,
      "day10test3", 1,
      "day10test4", 1,
      "day10test5", 4,
      "day10test6", 8,
      "day10test7", 10
    );
  }
}
