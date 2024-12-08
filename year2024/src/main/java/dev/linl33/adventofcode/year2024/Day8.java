package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Day8 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
    new Day8().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var antMap = new HashMap<Integer, List<Point2D>>();

    var dim = lines.length;
    for (int y = 0; y < dim; y++) {
      for (int x = 0; x < dim; x++) {
        var pt = lines[y].codePointAt(x);
        if (pt == '.') {
          continue;
        }

        antMap.putIfAbsent(pt, new ArrayList<>());
        var list = antMap.get(pt);
        list.add(new Point2D(x, y));
      }
    }

    var anti = new HashSet<Point2D>();

    for (var freq : antMap.keySet()) {
      var locations = antMap.get(freq);

      for (var leftIdx = 0; leftIdx < locations.size(); leftIdx++) {
        var left = locations.get(leftIdx);

        for (var rightIdx = leftIdx + 1; rightIdx < locations.size(); rightIdx++) {
          var right = locations.get(rightIdx);

          var dx = right.x() - left.x();
          var dy = right.y() - left.y();

          anti.add(left.translate(-dx, -dy));
          anti.add(right.translate(dx, dy));
        }
      }
    }

    anti.removeIf(pt -> pt.x() < 0 || pt.y() < 0 || pt.x() >= dim || pt.y() >= dim);
    return anti.size();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var antMap = new HashMap<Integer, List<Point2D>>();

    var dim = lines.length;
    for (int y = 0; y < dim; y++) {
      for (int x = 0; x < dim; x++) {
        var pt = lines[y].codePointAt(x);
        if (pt == '.') {
          continue;
        }

        antMap.putIfAbsent(pt, new ArrayList<>());
        var list = antMap.get(pt);
        list.add(new Point2D(x, y));
      }
    }

    var anti = new HashSet<Point2D>();

    for (var freq : antMap.keySet()) {
      var locations = antMap.get(freq);

      for (var leftIdx = 0; leftIdx < locations.size(); leftIdx++) {
        var left = locations.get(leftIdx);

        for (var rightIdx = leftIdx + 1; rightIdx < locations.size(); rightIdx++) {
          var right = locations.get(rightIdx);

          var dx = right.x() - left.x();
          var dy = right.y() - left.y();

          for (int i = 1; i < dim; i++) {
            anti.add(left.translate(dx * i, dy * i));
            anti.add(right.translate(dx * -i, dy * -i));
          }
        }
      }
    }

    anti.removeIf(pt -> pt.x() < 0 || pt.y() < 0 || pt.x() >= dim || pt.y() >= dim);
    return anti.size();
  }
}
