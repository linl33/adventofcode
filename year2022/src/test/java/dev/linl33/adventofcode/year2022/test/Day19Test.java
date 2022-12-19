package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day19;

import java.util.Map;

public class Day19Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day19();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 600,
        "day19test1", 33
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 6000,
        "day19test1", 3472
    );
  }
}
