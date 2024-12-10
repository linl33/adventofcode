package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.function.ToIntFunction;

public class Day10 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
    new Day10().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var visited = new HashSet<Point2D>();
    return solve(reader, visited::clear, pt -> visited.add(pt) ? 1 : 0);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return solve(reader, () -> {}, _ -> 1);
  }

  private static int solve(BufferedReader reader, Runnable onTrailheadStart, ToIntFunction<Point2D> onEndReached) {
    var lines = reader.lines().toArray(String[]::new);
    var dim = lines.length;

    var total = 0;

    for (var y = 0; y < lines.length; y++) {
      var row = lines[y];

      for (var x = 0; x < dim; x++) {
        var pt = row.charAt(x);

        if (pt != '0') {
          continue;
        }

        onTrailheadStart.run();
        total += dfs(lines, new Point2D(x, y), onEndReached);
      }
    }

    return total;
  }

  private static int dfs(String[] grid, Point2D start, ToIntFunction<Point2D> onEndReached) {
    var startVal = grid[start.y()].charAt(start.x());

    if (startVal == '9') {
      return onEndReached.applyAsInt(start);
    }

    var total = 0;

    for (int deltaY = -1; deltaY <= 1; deltaY++) {
      for (int deltaX = -1; deltaX <= 1; deltaX++) {
        if (deltaY != 0 && deltaX != 0) {
          continue;
        }

        if (deltaX == deltaY) {
          continue;
        }

        var pt = start.translate(deltaX, deltaY);

        if (pt.x() >= 0 && pt.x() < grid.length && pt.y() >= 0 && pt.y() < grid.length) {
          if (grid[pt.y()].charAt(pt.x()) - startVal == 1) {
            total += dfs(grid, pt, onEndReached);
          }
        }
      }
    }

    return total;
  }
}
