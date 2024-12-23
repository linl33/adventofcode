package dev.linl33.adventofcode.year2024.test;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2024.Day23;

import java.util.Map;

public class Day23Test implements AdventSolutionTest<Integer, String> {
  @Override
  public AdventSolution<Integer, String> newSolutionInstance() {
    return new Day23();
  }

  @Override
  public Map<Object, Integer> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), 1194,
         "day23test1", 7
    );
  }

  @Override
  public Map<Object, String> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), "bd,bu,dv,gl,qc,rn,so,tm,wf,yl,ys,ze,zr",
        "day23test1", "co,de,ka,ta"
    );
  }
}
