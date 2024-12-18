package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day18;

import java.util.Map;

public class Day18Test implements AdventSolutionTest<Integer, String> {
  @Override
  public AdventSolution<Integer, String> newSolutionInstance() {
    return new Day18();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 284
        // TODO:
        // "day18test1", 22
    );
  }

  @Override
  public Map<Object, String> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "51,50"
       // TODO:
       // "day18test1", "6,1"
    );
  }
}
