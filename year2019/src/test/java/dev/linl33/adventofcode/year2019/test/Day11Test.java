package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day11;

import java.util.Map;

class Day11Test implements AdventSolutionTest<Integer, Long[][]> {
  @Override
  public AdventSolution<Integer, Long[][]> newSolutionInstance() {
    return new Day11();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 2041);
  }

  @Override
  public Map<Object, Long[][]> getPart2Cases() {
    var expected = new Long[][]{
        {0L, 1L, 1L, 1L, 1L, 0L, 1L, 1L, 1L, 0L, 0L, 1L, 1L, 1L, 1L, 0L, 1L, 1L, 1L, 0L, 0L, 1L, 0L, 0L, 1L, 0L, 1L, 1L, 1L, 1L, 0L, 1L, 1L, 1L, 1L, 0L, 1L, 1L, 1L, 0L, 0L, 0L, null},
        {null, 0L, 0L, 0L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 0L},
        {null, 0L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 1L, 0L, 1L, 1L, 0L, 0L, 0L, 1L, 1L, 1L, 0L, 0L, 0L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 0L},
        {0L, 0L, 1L, 0L, 0L, 0L, 1L, 1L, 1L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 1L, 1L, 0L, 0L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 1L, 1L, 1L, 0L, 0L, 0L, null},
        {0L, 1L, 0L, 0L, 0L, 0L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 1L, 0L, 0L, 0L, 0L, 1L, 0L, 1L, 0L, 0L, null, null},
        {null, 1L, 1L, 1L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, 1L, 1L, 1L, 1L, 0L, 1L, 0L, 0L, 0L, 0L, 1L, 0L, 0L, 1L, 0L, 1L, 1L, 1L, 1L, 0L, 1L, 1L, 1L, 1L, 0L, 1L, 0L, 0L, 1L, 0L, null, null}
    };

    return Map.of(newSolutionInstance().getPart2Resource(), expected);
  }
}
