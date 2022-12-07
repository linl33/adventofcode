package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day7;

import java.util.Map;

public class Day7Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day7();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        "day7", 1306611L,
        "day7test1", 95437L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
        return Map.of(
        "day7", 13210366L,
        "day7test1", 24933642L
    );
  }
}
