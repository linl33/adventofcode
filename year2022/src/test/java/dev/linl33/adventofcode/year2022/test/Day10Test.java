package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day10;

import java.util.Map;

public class Day10Test implements AdventSolutionTest<Integer, boolean[]> {
  @Override
  public AdventSolution<Integer, boolean[]> newSolutionInstance() {
    return new Day10();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        "day10", 13220,
        "day10test1", 13140
    );
  }

  @Override
  public Map<Object, boolean[]> getPart2Cases() {
    return Map.of(
        "day10", new boolean[] { true, true, true, false, false, true, false, false, true, false, false, true, true, false, false, true, false, false, true, false, true, false, false, true, false, true, true, true, false, false, true, true, true, true, false, true, false, false, true, false, true, false, false, true, false, true, false, false, true, false, true, false, false, true, false, true, false, true, false, false, true, false, false, true, false, true, false, false, true, false, true, false, false, false, false, true, false, true, false, false, true, false, false, true, false, true, false, false, true, false, true, false, false, true, false, true, true, false, false, false, true, true, true, true, false, true, true, true, false, false, true, true, true, false, false, true, true, false, false, false, true, true, true, false, false, true, false, false, true, false, true, true, true, true, false, true, false, true, false, false, true, false, false, true, false, true, false, false, true, false, true, false, false, false, false, true, false, true, false, false, true, false, true, false, false, true, false, false, true, false, true, false, false, true, false, true, false, true, false, false, true, false, false, true, false, true, false, false, true, false, true, false, false, false, false, true, false, true, false, false, true, false, false, true, false, false, true, true, false, false, true, false, false, true, false, true, false, false, true, false, true, false, false, true, false, true, true, true, false, false, true, true, true, true, false, true, false, false, true, false },
        "day10test1", new boolean[] { true, true, false, false, true, true, false, false, true, true, false, false, true, true, false, false, true, true, false, false, true, true, false, false, true, true, false, false, true, true, false, false, true, true, false, false, true, true, false, false, true, true, true, false, false, false, true, true, true, false, false, false, true, true, true, false, false, false, true, true, true, false, false, false, true, true, true, false, false, false, true, true, true, false, false, false, true, true, true, false, true, true, true, true, false, false, false, false, true, true, true, true, false, false, false, false, true, true, true, true, false, false, false, false, true, true, true, true, false, false, false, false, true, true, true, true, false, false, false, false, true, true, true, true, true, false, false, false, false, false, true, true, true, true, true, false, false, false, false, false, true, true, true, true, true, false, false, false, false, false, true, true, true, true, true, false, false, false, false, false, true, true, true, true, true, true, false, false, false, false, false, false, true, true, true, true, true, true, false, false, false, false, false, false, true, true, true, true, true, true, false, false, false, false, false, false, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false, false, false, false, true, true, true, true, true, true, true, false, false, false, false, false, false, false, true, true, true, true, true, true, true, false, false, false, false, false }
    );
  }
}
