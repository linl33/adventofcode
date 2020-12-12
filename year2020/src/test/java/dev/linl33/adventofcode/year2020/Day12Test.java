package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Day12Test implements AdventSolutionTest<Integer, Integer> {

  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day12();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1589,
        "day12test1", 25
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 23960,
        "day12test1", 286
    );
  }
}
