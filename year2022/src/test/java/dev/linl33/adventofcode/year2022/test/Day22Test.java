package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2022.Day15;
import dev.linl33.adventofcode.year2022.Day22;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day22Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day22();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1484,
        "day22test1", 6032
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 142228
        // requires additional parameter, tested below
        // "day22test1", 5031
    );
  }

  @Test
  void testPart2Example() {
    assertEquals(
        5031,
        newSolutionInstance().run(
            (Day22 day22, BufferedReader reader) -> day22.part2Internal(reader, 0),
            new ClasspathResourceIdentifier("day22test1")
        )
    );
  }
}
