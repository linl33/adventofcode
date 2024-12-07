package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day7;

import java.util.Map;

public class Day7Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day7();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 850435817339L,
        "day7test1", 3749L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 104824810233437L,
        "day7test1", 11387L
    );
  }
}
