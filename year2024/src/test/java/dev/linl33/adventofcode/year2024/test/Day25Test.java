package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day25;

import java.util.Map;

public class Day25Test implements AdventSolutionTest<Integer, Void> {
  @Override
  public AdventSolution<Integer, Void> newSolutionInstance() {
    return new Day25();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 3090,
         "day25test1", 3
    );
  }

  @Override
  public Map<Object, Void> getPart2Cases() {
    return null;
  }
}
