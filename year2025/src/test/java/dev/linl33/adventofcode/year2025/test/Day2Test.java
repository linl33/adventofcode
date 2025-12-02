package dev.linl33.adventofcode.year2025.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2025.Day2;

import java.util.Map;

public class Day2Test implements AdventSolutionTest<Long, Long> {
  @Override
  public AdventSolution<Long, Long> newSolutionInstance() {
    return new Day2();
  }

  @Override
  public Map<Object, Long> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 32976912643L,
        "day2test1", 1227775554L,
        // source: https://www.reddit.com/r/adventofcode/comments/1pc2h1l/2025_day_2_challenge_input/
        "day2test2", 121412594604227157L
    );
  }

  @Override
  public Map<Object, Long> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 54446379122L,
        "day2test1", 4174379265L,
        // source: https://www.reddit.com/r/adventofcode/comments/1pc2h1l/2025_day_2_challenge_input/
        "day2test2", 122614329477263799L
    );
  }
}
