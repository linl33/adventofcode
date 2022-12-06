package dev.linl33.adventofcode.year2022.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.testlib.StringResourceIdentifier;
import dev.linl33.adventofcode.year2022.Day6;

import java.util.Map;

class Day6Test implements AdventSolutionTest<Integer, Integer> {
  @Override
  public AdventSolution<Integer, Integer> newSolutionInstance() {
    return new Day6();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1275,
        new StringResourceIdentifier("mjqjpqmgbljsphdztnvjfqwrcgsmlb"), 7,
        new StringResourceIdentifier("bvwbjplbgvbhsrlpgdmjqwftvncz"), 5,
        new StringResourceIdentifier("nppdvjthqldpwncqszvftbrmjlhg"), 6,
        new StringResourceIdentifier("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"), 10,
        new StringResourceIdentifier("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"), 11
    );
  }

  @Override
  public Map<Object, Integer> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), 3605,
        new StringResourceIdentifier("mjqjpqmgbljsphdztnvjfqwrcgsmlb"), 19,
        new StringResourceIdentifier("bvwbjplbgvbhsrlpgdmjqwftvncz"), 23,
        new StringResourceIdentifier("nppdvjthqldpwncqszvftbrmjlhg"), 23,
        new StringResourceIdentifier("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"), 29,
        new StringResourceIdentifier("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"), 26
    );
  }
}
