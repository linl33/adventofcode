package dev.linl33.adventofcode.year2019.test;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2019.Day24;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Day24Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day24();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 18350099,
        "day24test1", 2129920
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 2037);
  }

  @Test
  void testPart2Example(Day24 day24) {
    assertEquals(99, day24.run((Day24 instance, BufferedReader reader) -> instance.part2Internal(reader, 10), new ClasspathResourceIdentifier("day24test1")));
  }
}
