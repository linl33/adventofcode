package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day7;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test implements AdventSolutionTest<String, Integer> {
  @Override
  public AdventSolution<String, Integer> newSolutionInstance() {
    return new Day7();
  }

  @Override
  public Map<String, String> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "ACHOQRXSEKUGMYIWDZLNBFTJVP",
        "day7test", "CABDFE"
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 985
    );
  }

  @Test
  void testPart2Simple(Day7 day7) {
    assertEquals(
        Integer.valueOf(15),
        day7.run(
            (solutionInstance, reader) -> ((Day7) solutionInstance).part2Internal(reader, true),
            "day7test"
        )
    );
  }
}
