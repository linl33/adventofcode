package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.point.Point;
import dev.linl33.adventofcode.lib.point.Point3D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Day18 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day18().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);
    var points = new ArrayList<Point3D>(lines.length);

    for (var line : lines) {
      var parts = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).iterator();
      var pt = new Point3D(parts.nextInt(), parts.nextInt(), parts.nextInt());
      points.add(pt);
    }

    var sum = 0;

    for (int i = 0; i < points.size(); i++) {
      var pt1 = points.get(i);
      sum += 6;

      for (int j = i + 1; j < points.size(); j++) {
        var pt2 = points.get(j);
        var distance = pt1.manhattanDistance(pt2);
        if (distance == 1) {
          sum -= 2;
        }
      }
    }

    return sum;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);
    var points = new ArrayList<Point3D>(lines.length);

    var xMin = 0;
    var xMax = 0;
    var yMin = 0;
    var yMax = 0;
    var zMin = 0;
    var zMax = 0;

    for (var line : lines) {
      var parts = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).iterator();
      var x = parts.nextInt();
      var y = parts.nextInt();
      var z = parts.nextInt();
      points.add(new Point3D(x, y, z));
      
      xMin = Math.min(xMin, x);
      xMax = Math.max(xMax, x);
      yMin = Math.min(yMin, y);
      yMax = Math.max(yMax, y);
      zMin = Math.min(zMin, z);
      zMax = Math.max(zMax, z);
    }

    var queue = new ArrayDeque<Point3D>();
    queue.add(new Point3D(xMin - 1, yMin - 1, zMin - 1));

    var sum = 0;
    var visited = new HashSet<Point3D>();

    while (!queue.isEmpty()) {
      var curr = queue.remove();

      for (int z = -1; z <= 1; z++) {
        for (int y = -1; y <= 1; y++) {
          for (int x = -1; x <= 1; x++) {
            var delta = new Point3D(x, y, z);
            if (Point.ORIGIN_3D.manhattanDistance(delta) != 1) {
              continue;
            }

            var next = curr.translate(delta);
            if (visited.contains(next)) {
              continue;
            }

            if (points.contains(next)) {
              sum++;
              continue;
            }

            if (next.x() < xMin - 1 || next.x() > xMax + 1) {
              continue;
            }
            if (next.y() < yMin - 1 || next.y() > yMax + 1) {
              continue;
            }
            if (next.z() < zMin - 1 || next.z() > zMax + 1) {
              continue;
            }

            queue.add(next);
            visited.add(next);
          }
        }
      }
    }

    return sum;
  }
}
