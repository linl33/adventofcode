package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.graph.GraphPath;
import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;

public class Day18 extends AdventSolution2024<Integer, String> {
  public static void main() {
    new Day18().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var dim = 71;
    var grid = new HashSet<Point2D>();

    for (int i = 0; i < 1024; i++) {
      var commaIdx = lines[i].indexOf(",");
      grid.add(new Point2D(
        Integer.parseInt(lines[i], 0, commaIdx, 10),
        Integer.parseInt(lines[i], commaIdx + 1, lines[i].length(), 10)
      ));
    }

    var path = GraphUtil.aStar(
      new Point2D(0, 0),
      new Point2D(dim - 1, dim - 1),
      (pt) -> {
        var neighbors = new ArrayList<Point2D>(4);

        if (pt.x() > 0) {
          neighbors.add(new Point2D(pt.x() - 1, pt.y()));
        }

        if (pt.y() > 0) {
          neighbors.add(new Point2D(pt.x(), pt.y() - 1));
        }

        if (pt.x() < dim - 1) {
          neighbors.add(new Point2D(pt.x() + 1, pt.y()));
        }

        if (pt.y() < dim - 1) {
          neighbors.add(new Point2D(pt.x(), pt.y() + 1));
        }

        neighbors.removeIf(grid::contains);
        return neighbors;
      }
    );

    return path.orElseThrow().length();
  }

  @Override
  public String part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var dim = 71;
    var grid = new HashSet<Point2D>();
    GraphPath<Point2D> shortestPath = null;

    for (var i = 0; i < lines.length; i++) {
      var commaIdx = lines[i].indexOf(",");
      var corruptedPt = new Point2D(
        Integer.parseInt(lines[i], 0, commaIdx, 10),
        Integer.parseInt(lines[i], commaIdx + 1, lines[i].length(), 10)
      );
      grid.add(corruptedPt);

      if (shortestPath != null && !shortestPath.path().containsKey(corruptedPt)) {
        continue;
      }

      var path = GraphUtil.aStar(
        new Point2D(0, 0),
        new Point2D(dim - 1, dim - 1),
        (pt) -> {
          var neighbors = new ArrayList<Point2D>(4);

          if (pt.x() > 0) {
            neighbors.add(new Point2D(pt.x() - 1, pt.y()));
          }

          if (pt.y() > 0) {
            neighbors.add(new Point2D(pt.x(), pt.y() - 1));
          }

          if (pt.x() < dim - 1) {
            neighbors.add(new Point2D(pt.x() + 1, pt.y()));
          }

          if (pt.y() < dim - 1) {
            neighbors.add(new Point2D(pt.x(), pt.y() + 1));
          }

          neighbors.removeIf(grid::contains);
          return neighbors;
        }
      );

      if (path.isEmpty()) {
        return lines[i];
      } else {
        shortestPath = path.get();
      }
    }

    return null;
  }
}
