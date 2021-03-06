package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.Part1Source;
import dev.linl33.adventofcode.year2020.Day6;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day6();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 6310,
        "day6test1", 11
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 3193,
        "day6test1", 6
    );
  }

  @ParameterizedTest
  @Part1Source
  void testPart1ByLogicalOr(String resource, int expected, AdventSolution<Integer, Integer> instance) {
    assertEquals(expected, instance.run(Day6::part1ByLogicalOr, resource));
  }
}
