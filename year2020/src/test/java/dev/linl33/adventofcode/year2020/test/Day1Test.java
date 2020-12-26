package dev.linl33.adventofcode.year2020.test;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.TestCaseSource;
import dev.linl33.adventofcode.testlib.TestPart;
import dev.linl33.adventofcode.year2020.Day1;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day1();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 902451,
        "day1test1", 514579
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 85555470,
        "day1test1", 241861950
    );
  }

  @ParameterizedTest
  @TestCaseSource(value = {TestPart.PART_1, TestPart.PART_2}, extraArgs = @ValueSource(ints = {2, 2, 3, 3}))
  void testSolveByIntStream(ResourceIdentifier resource, int expected, int numbers, Day1 instance) throws Exception {
    assertEquals(expected, instance.run(adaptSolveByIntStream(numbers), resource));
  }

  private static ThrowingBiFunction<Day1, BufferedReader, Integer> adaptSolveByIntStream(int numbers) {
    return (Day1 day1, BufferedReader reader) -> day1.solveByIntStream(reader, numbers);
  }
}
