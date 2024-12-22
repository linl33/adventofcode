package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day22;

import java.util.Map;

public class Day22Test implements AdventSolutionTest<Long, Integer> {
  @Override
  public AdventSolution<Long, Integer> newSolutionInstance() {
    return new Day22();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 17005483322L,
      "day22test1", 37327623L
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1910,
      "day22test2", 23
    );
  }
}
