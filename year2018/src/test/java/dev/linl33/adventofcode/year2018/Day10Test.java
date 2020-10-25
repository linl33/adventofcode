package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;

import java.util.Map;

class Day10Test implements AdventSolutionTest<Character[][], Integer> {
  @Override
  public AdventSolution<Character[][], Integer> newSolutionInstance() {
    return new Day10();
  }

  @Override
  public Map<String, Character[][]> getPart1Cases() {
    var expected = new Character[][] {
        {'█', '█', '█', '█', '█', '█', ' ', ' ', ' ', ' ', ' ', '█', '█', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', '█', '█', '█', ' ', ' ', ' ', ' ', '█', '█', '█', '█', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█'},
        {'█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', '█', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', '█', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█'},
        {'█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', '█', '█', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', '█', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', '█', ' '},
        {'█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', '█', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', '█', ' '},
        {'█', '█', '█', '█', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', '█', '█', ' ', ' ', ' ', ' ', '█', ' ', '█', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', '█', ' ', ' '},
        {'█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', '█', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', '█', ' ', ' ', ' ', ' ', '█', '█', ' ', ' '},
        {'█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', '█', ' '},
        {'█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', '█', ' ', ' ', ' ', '█', ' ', ' ', '█', ' '},
        {'█', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', '█', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', '█', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█'},
        {'█', '█', '█', '█', '█', '█', ' ', ' ', ' ', '█', '█', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', ' ', '█', '█', '█', '█', ' ', ' ', ' ', ' ', '█', '█', '█', '█', ' ', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█', ' ', ' ', '█', ' ', ' ', ' ', ' ', '█'}
    };

    return Map.of(newSolutionInstance().getPart2Resource(), expected);
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 10612);
  }
}