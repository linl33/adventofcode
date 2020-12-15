package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day15;

import java.util.Map;

class Day15Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day15();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 870,
        "string:0,3,6", 436,
        "string:1,3,2", 1,
        "string:2,1,3", 10,
        "string:1,2,3", 27,
        "string:2,3,1", 78,
        "string:3,2,1", 438,
        "string:3,1,2", 1836
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 9136,
        "string:0,3,6", 175594,
        "string:1,3,2", 2578,
        "string:2,1,3", 3544142,
        "string:1,2,3", 261214,
        "string:2,3,1", 6895259,
        "string:3,2,1", 18,
        "string:3,1,2", 362
    );
  }
}
