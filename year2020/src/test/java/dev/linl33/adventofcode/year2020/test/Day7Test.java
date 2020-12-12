package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day7;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Day7Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day7();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 169,
        "day7test1", 4
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 82372,
        "day7test1", 32,
        "day7test2", 126
    );
  }

  @ParameterizedTest
  @CsvSource({
      "day7, 169",
      "day7test1, 4"
  })
  void testPart1ByMutableGraph(String resource, int expected, Day7 instance) {
    assertEquals(expected, instance.run(Day7::part1ByMutableGraph, resource));
  }

  @ParameterizedTest
  @CsvSource({
      "day7, 82372",
      "day7test1, 32",
      "day7test2, 126"
  })
  void testPart2ByMutableGraph(String resource, int expected, Day7 instance) {
    assertEquals(expected, instance.run(Day7::part2ByMutableGraph, resource));
  }
}
