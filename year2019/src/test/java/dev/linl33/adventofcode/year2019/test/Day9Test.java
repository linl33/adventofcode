package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day9;

import java.util.Map;

class Day9Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day9();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 2955820355L);
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 46643L);
  }
}
