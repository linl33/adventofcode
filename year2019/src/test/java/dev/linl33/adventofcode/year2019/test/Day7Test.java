package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.Part2Source;
import dev.linl33.adventofcode.year2019.Day7;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day7();
  }

  @Override
  public Map<String, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 30940L,
        "day7test1", 43210L,
        "day7test2", 54321L,
        "day7test3", 65210L
    );
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 76211147L,
        "day7test4", 139629729L,
        "day7test5", 18216L
    );
  }

  @ParameterizedTest
  @Part2Source
  void part2SingleThread(String resource, long expected, AdventSolution<Long, Long> day7) {
    assertEquals(
        expected,
        day7.run(Day7::part2SingleThread, resource)
    );
  }
}
