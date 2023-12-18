package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Day18 extends AdventSolution2023<Long, Long> {
  public static void main(String[] args) {
    new Day18().benchmark(JmhBenchmarkOption.PART_2);
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var curr = new Point2D(0, 0);
    var dug = new ArrayList<Point2D>();
    dug.add(curr);

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var parts = line.split(" ", 3);
      var dir = parts[0].charAt(0);
      var mag = Integer.parseInt(parts[1]);

      if (dir == 'U') {
        curr = curr.translate(0, -mag);
        dug.add(curr);
      } else if (dir == 'D') {
        curr = curr.translate(0, mag);
        dug.add(curr);
      } else if (dir == 'R') {
        curr = curr.translate(mag, 0);
        dug.add(curr);
      } else if (dir == 'L') {
        curr = curr.translate(-mag, 0);
        dug.add(curr);
      }
    }

    return findArea(dug);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var curr = new Point2D(0, 0);
    var dug = new ArrayList<Point2D>();
    dug.add(curr);

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var parts = line.split(" ", 3);

      var dir = switch (parts[2].charAt(7)) {
        case '0' -> 'R';
        case '1' -> 'D';
        case '2' -> 'L';
        case '3' -> 'U';
        default -> throw new IllegalStateException();
      };

      var mag = Integer.parseInt(parts[2], 2, 7, 16);

      if (dir == 'U') {
        curr = curr.translate(0, -mag);
        dug.add(curr);
      } else if (dir == 'D') {
        curr = curr.translate(0, mag);
        dug.add(curr);
      } else if (dir == 'R') {
        curr = curr.translate(mag, 0);
        dug.add(curr);
      } else if (dir == 'L') {
        curr = curr.translate(-mag, 0);
        dug.add(curr);
      }
    }

    return findArea(dug);
  }

  private static long findArea(List<Point2D> pts) {
    var first = pts.getFirst();
    var last = pts.getLast();
    pts.addFirst(last);
    pts.add(first);

    var sum = 0L;
    for (int i = 1; i < pts.size() - 1; i++) {
      var pt = pts.get(i);
      var prev = pts.get(i - 1);
      var next = pts.get(i + 1);

      sum += (long) pt.x() * (next.y() - prev.y());
    }

    sum /= 2;

    var borderArea = 0L;
    for (int i = 1; i < pts.size() - 1; i++) {
      var pt = pts.get(i);
      var next = pts.get(i + 1);
      borderArea += Math.abs(pt.x() - next.x()) + Math.abs(pt.y() - next.y());
    }

    return sum + borderArea / 2 + 1;
  }
}
