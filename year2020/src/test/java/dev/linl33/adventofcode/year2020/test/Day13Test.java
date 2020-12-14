package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.Part2Source;
import dev.linl33.adventofcode.year2020.Day13;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Day13Test implements AdventSolutionTest<Integer, Long> {
  @Override
  public AdventSolution<Integer, Long> newSolutionInstance() {
    return new Day13();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 3464,
        "day13test1", 295
    );
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 760171380521445L,
        "day13test1", 1068781L,
        "string:0\n17,x,13,19", 3417L,
        "string:0\n67,7,59,61", 754018L,
        "string:0\n67,x,7,59,61", 779210L,
        "string:0\n67,7,x,59,61", 1261476L,
        "string:0\n1789,37,47,1889", 1202161486L
    );
  }

  @ParameterizedTest
  @Part2Source
  void testPart2WithoutPrimeAssumption(String resource, long expected, AdventSolution<Integer, Long> day13) {
    assertEquals(
        expected,
        day13.run(
            (Day13 inst, BufferedReader reader) -> inst.part2ByCrt(reader, false),
            resource
        )
    );
  }

  @ParameterizedTest
  @Part2Source
  @Disabled
  void testPart2Iterative(String resource, long expected, AdventSolution<Integer, Long> day13) {
    assertEquals(expected, day13.run(Day13::part2Iterative, resource));
  }

  @ParameterizedTest
  @Part2Source
  @Disabled
  void testPart2LinearSystem(String resource, long expected, AdventSolution<Integer, Long> day13) {
    assertEquals(expected, day13.run(Day13::part2ByLinearSystem, resource));
  }
}
