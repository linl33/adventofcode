package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.Part1Source;
import dev.linl33.adventofcode.testlib.Part2Source;
import dev.linl33.adventofcode.year2020.Day7;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day7();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 169,
        "day7test1", 4
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 82372,
        "day7test1", 32,
        "day7test2", 126
    );
  }

  @ParameterizedTest
  @Part1Source
  void testPart1ByMutableGraph(ResourceIdentifier resource, int expected, Day7 instance) {
    assertEquals(expected, instance.run((ThrowingBiFunction<Day7, BufferedReader, Integer>) Day7::part1ByMutableGraph, resource));
  }

  @ParameterizedTest
  @Part2Source
  void testPart2ByMutableGraph(ResourceIdentifier resource, int expected, Day7 instance) {
    assertEquals(expected, instance.run((ThrowingBiFunction<Day7, BufferedReader, Integer>) Day7::part2ByMutableGraph, resource));
  }
}
