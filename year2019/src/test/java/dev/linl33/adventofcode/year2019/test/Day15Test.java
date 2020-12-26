package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.Part2Source;
import dev.linl33.adventofcode.year2019.Day15;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day15();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 234);
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 292);
  }

  @ParameterizedTest
  @Part2Source
  void part2ByPathFinding(ResourceIdentifier resource, int expected, Day15 day15) {
    assertEquals(expected, day15.run((ThrowingBiFunction<Day15, BufferedReader, Integer>) Day15::part2ByPathFinding, resource));
  }
}
