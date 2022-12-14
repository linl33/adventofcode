package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day13;

import java.util.Map;

public class Day13Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day13();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        "day13", 6076,
        "day13test1", 13
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        "day13", 24805,
        "day13test1", 140
    );
  }
}
