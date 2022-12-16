package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.TestCaseSource;
import dev.linl33.adventofcode.testlib.TestPart;
import dev.linl33.adventofcode.year2022.Day15;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day15Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day15();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 5878678L,
        "day15test1", 26L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 11796491041245L,
        "day15test1", 56000011L
    );
  }

  @Test
  void testPart1Example() {
    assertEquals(
        26L,
        newSolutionInstance().run(
            (Day15 day15, BufferedReader reader) -> day15.part1Row(reader, 10),
            new ClasspathResourceIdentifier("day15test1")
        )
    );
  }

  @Override
  public Map<TestPart, Map<ResourceIdentifier, String>> getDisabledTests() {
    return Map.of(
        TestPart.PART_1,
        Map.of(
            new ClasspathResourceIdentifier("day15test1"), "Requires additional parameter, covered by custom test."
        )
    );
  }
}
