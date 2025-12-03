package dev.linl33.adventofcode.year2025.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2025.Day3;

import java.util.Map;

public class Day3Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day3();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
      newSolutionInstance().getPart1Resource(), 17074L,
      "day3test1", 357L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
      newSolutionInstance().getPart2Resource(), 169512729575727L,
      "day3test1", 3121910778619L
    );
  }
}
