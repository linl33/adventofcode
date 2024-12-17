package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day17;

import java.util.Map;

public class Day17Test implements AdventSolutionTest<String, Long> {
  @Override
  public AdventSolution<String, Long> newSolutionInstance() {
    return new Day17();
  }

  @Override
  public Map<Object, String> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "7,0,3,1,2,6,3,7,1",
      "day17test1", "4,6,3,5,6,3,5,2,1,0",
      "day17test2", "5,7,3,0"
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 109020013201563L
      // TODO:
//      "day17test2", 117440L
    );
  }
}
