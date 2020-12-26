package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2020.Day9;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day9();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(newSolutionInstance().getPart1Resource(), 373803594L);
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 51152360L);
  }

  @Test
  void testPart1WithPreamble5(Day9 instance) {
    assertEquals(
        127L,
        instance.<Long, Day9>run(
            (Day9 __, BufferedReader reader) -> Day9.solvePart1(reader, 5),
            new ClasspathResourceIdentifier("day9test1")
        )
    );
  }

  @Test
  void testPart2WithPreamble5(Day9 instance) {
    assertEquals(
        62L,
        instance.<Long, Day9>run(
            (Day9 __, BufferedReader reader) -> Day9.solvePart2(reader, 5),
            new ClasspathResourceIdentifier("day9test1")
        )
    );
  }
}
