package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day11;

import java.util.Map;

public class Day11Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day11();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 222461L,
        "day11test1", 55312L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 264350935776416L,
        "day11test1", 65601038650482L
    );
  }
}
