package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day12;

import java.util.Map;

public class Day12Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day12();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1467094,
        "day12test1", 140,
        "day12test2", 772,
        "day12test3", 1930,
        "day12test4", 692,
        "day12test5", 1184
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 881182,
      "day12test1", 80,
      "day12test2", 436,
      "day12test3", 1206,
      "day12test4", 236,
      "day12test5", 368
    );
  }
}
