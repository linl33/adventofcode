package dev.linl33.adventofcode.year2018.test;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.testlib.AdventSolutionTest;
import dev.linl33.adventofcode.year2018.Day13;

import java.util.Map;

class Day13Test implements AdventSolutionTest<Point2D, Point2D> {
  @Override
  public AdventSolution<Point2D, Point2D> newSolutionInstance() {
    return new Day13();
  }

  @Override
  public Map<Object, Point2D> getPart1Cases() {
    return Map.of(
        newSolutionInstance().getPart1Resource(), new Point2D(118, 112),
        "day13test1", new Point2D(7, 3)
    );
  }

  @Override
  public Map<Object, Point2D> getPart2Cases() {
    return Map.of(
        newSolutionInstance().getPart2Resource(), new Point2D(50, 21),
        "day13test2", new Point2D(6, 4),
        "day13test3", new Point2D(4, 2)
    );
  }
}
