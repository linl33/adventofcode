package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day9();
  }

  @Override
  public Map<String, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 373803594L);
  }

  @Override
  public Map<String, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 51152360L);
  }

  @Test
  void testPart1WithPreamble5(Day9 instance) {
    assertEquals(
        127L,
        instance.<Long, Day9>run((__, reader) -> Day9.solvePart1(reader, 5), "day9test1")
    );
  }

  @Test
  void testPart2WithPreamble5(Day9 instance) {
    assertEquals(
        62L,
        instance.<Long, Day9>run((__, reader) -> Day9.solvePart2(reader, 5), "day9test1")
    );
  }
}
