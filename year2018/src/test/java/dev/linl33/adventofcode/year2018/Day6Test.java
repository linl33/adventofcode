package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day6Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day6();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 3969,
        "day6test", 17
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(newSolutionInstance().getPart2Resource(), 42123);
  }

  @Test
  void part2SmallRadius(Day6 day6) {
    assertEquals(
        Integer.valueOf(16),
        day6.run(
            (solutionInstance, reader) -> ((Day6) solutionInstance).part2Internal(reader, 32),
            "day6test"
        )
    );
  }
}
