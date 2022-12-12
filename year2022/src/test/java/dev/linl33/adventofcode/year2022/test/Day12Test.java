package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day12;

import java.util.Map;

public class Day12Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day12();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        "day12", 481,
        "day12test1", 31
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        "day12", 480,
        "day12test1", 29
    );
  }
}
