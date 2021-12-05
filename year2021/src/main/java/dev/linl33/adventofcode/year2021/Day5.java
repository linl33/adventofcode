package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.regex.Pattern;

public class Day5 extends AdventSolution2021<Integer, Integer> {
  private static final int GRID_WIDTH = 1000;
  private static final int GRID_HEIGHT = 1000;
  private static final Pattern SPLIT_PATTERN = Pattern.compile(",| -> ");

  public static void main(String[] args) {
    new Day5().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    return countOverlappingVents(lines, false);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    return countOverlappingVents(lines, true);
  }

  private static int countOverlappingVents(@NotNull String[] lines, boolean includeDiagonals) {
    var grid = new int[GRID_WIDTH * GRID_HEIGHT];
    var gridCount = 0;

    for (var line : lines) {
      var nums = SPLIT_PATTERN.split(line, 4);

      var x1 = Integer.parseInt(nums[0]);
      var y1 = Integer.parseInt(nums[1]);
      var x2 = Integer.parseInt(nums[2]);
      var y2 = Integer.parseInt(nums[3]);

      var deltaX = x2 - x1;
      var deltaY = y2 - y1;

      if (!includeDiagonals && deltaX != 0 && deltaY != 0) {
        continue;
      }

      var magnitude = deltaX == 0 ? Math.abs(deltaY) : Math.abs(deltaX);
      deltaX = Integer.signum(deltaX);
      deltaY = Integer.signum(deltaY);

      for (int i = 0; i <= magnitude; i++) {
        gridCount += grid[(y1 + deltaY * i) * GRID_WIDTH + (x1 + deltaX * i)]++ == 1 ? 1 : 0;
      }
    }

    return gridCount;
  }
}
