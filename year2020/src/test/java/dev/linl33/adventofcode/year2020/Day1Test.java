package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.BufferedReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day1Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day1();
  }

  @Override
  public Map<String, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 902451,
        "day1test1", 514579
    );
  }

  @Override
  public Map<String, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 85555470,
        "day1test1", 241861950
    );
  }

  @ParameterizedTest
  @CsvSource({
      "day1, 902451, 2",
      "day1test1, 514579, 2",
      "day1, 85555470, 3",
      "day1test1, 241861950, 3"
  })
  void testSolveByIntStream(String resource, int expected, int numbers, Day1 instance) {
    assertEquals(expected, instance.run(adaptSolveByIntStream(numbers), resource));
  }

  private static ThrowingBiFunction<Day1, BufferedReader, Integer> adaptSolveByIntStream(int numbers) {
    return (Day1 sInstance, BufferedReader reader) -> sInstance.solveByIntStream(reader, numbers);
  }
}
