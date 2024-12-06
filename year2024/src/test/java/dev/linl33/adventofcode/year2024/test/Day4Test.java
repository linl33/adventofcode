package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day4;

import java.util.Map;

public class Day4Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day4();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 2642,
        "day4test1", 4,
        "day4test2", 18
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1974,
        "day4test3", 9
    );
  }
}
