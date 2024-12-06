package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day5;

import java.util.Map;

public class Day5Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day5();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 5166,
        "day5test1", 143
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 4679,
        "day5test1", 123
    );
  }
}
