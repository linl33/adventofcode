package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day1;

import java.util.Map;

class Day1Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day1();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 531,
        "day1test1", 3,
        "day1test2", 0,
        "day1test3", -6
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 76787,
        "day1test4", 0,
      "day1test5", 10,
      "day1test6", 5,
      "day1test7", 14
    );
  }
}
