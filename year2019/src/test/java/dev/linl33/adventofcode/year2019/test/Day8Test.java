package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day8;

import java.util.Map;

class Day8Test implements AdventSolutionTest<Integer, char[][]> {
  @Override
  public AdventSolution<Integer, char[][]> newSolutionInstance() {
    return new Day8();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 1330);
  }

  @Override
  public Map<Object, char[][]> getPart2Cases() {
    var expected = new char[][] {
        {'1', '1', '1', '1', '0', '0', '1', '1', '0', '0', '1', '0', '0', '1', '0', '1', '1', '1', '1', '0', '1', '1', '1', '1', '0'},
        {'1', '0', '0', '0', '0', '1', '0', '0', '1', '0', '1', '0', '0', '1', '0', '1', '0', '0', '0', '0', '1', '0', '0', '0', '0'},
        {'1', '1', '1', '0', '0', '1', '0', '0', '1', '0', '1', '1', '1', '1', '0', '1', '1', '1', '0', '0', '1', '1', '1', '0', '0'},
        {'1', '0', '0', '0', '0', '1', '1', '1', '1', '0', '1', '0', '0', '1', '0', '1', '0', '0', '0', '0', '1', '0', '0', '0', '0'},
        {'1', '0', '0', '0', '0', '1', '0', '0', '1', '0', '1', '0', '0', '1', '0', '1', '0', '0', '0', '0', '1', '0', '0', '0', '0'},
        {'1', '0', '0', '0', '0', '1', '0', '0', '1', '0', '1', '0', '0', '1', '0', '1', '1', '1', '1', '0', '1', '0', '0', '0', '0'}
    };

    return Map.of(newSolutionInstance().getPart2Resource(), expected);
  }
}
