package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day8;

import java.util.Map;

class Day8Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day8();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1818,
        "day8test1", 21
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 368368,
        "day8test1", 8
    );
  }
}
