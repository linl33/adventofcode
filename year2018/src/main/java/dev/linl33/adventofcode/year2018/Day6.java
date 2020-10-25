package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.GeomUtil;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 extends AdventSolution2018<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    List<Point2D> points = reader
        .lines()
        .map(s -> {
          String[] split = s.split(", ");
          return new Point2D(split[0], split[1]);
        })
        .collect(Collectors.toList());

    var furthest = points
        .stream()
        .flatMapToInt(pt -> IntStream.of(pt.x(), pt.y()))
        .max()
        .orElseThrow(IllegalArgumentException::new);

    Map<Point2D, Integer> area1 = findArea(furthest * 2, points);
    Map<Point2D, Integer> area2 = findArea((int) (furthest * 2.5), points);

    area1.keySet().removeIf(pt -> !area1.get(pt).equals(area2.get(pt)));

    return Collections.max(area1.values());
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return part2Internal(reader, 10000);
  }

  public Integer part2Internal(BufferedReader reader, int totalDistRadius) {
    List<Point2D> points = reader
        .lines()
        .map(s -> {
          String[] split = s.split(", ");
          return new Point2D(split[0], split[1]);
        })
        .collect(Collectors.toList());

    var regionSize = 0;

    var gridSize = 1000;
    for (int x = -gridSize; x < gridSize; x++) {
      for (int y = -gridSize; y < gridSize; y++) {
        var currPt = new Point2D(x, y);

        var totalDist = 0;
        for (Point2D knownPoint : points) {
          totalDist += GeomUtil.manhattanDist(currPt, knownPoint);
        }

        if (totalDist < totalDistRadius) {
          regionSize++;
        }
      }
    }

    return regionSize;
  }

  private static Map<Point2D, Integer> findArea(int gridSize, List<Point2D> knownPoints) {
    var areaCount = new HashMap<Point2D, Integer>();

    for (int x = -gridSize / 10; x < gridSize; x++) {
      for (int y = -gridSize / 10; y < gridSize; y++) {
        var currPt = new Point2D(x, y);

        var minDist = Integer.MAX_VALUE;
        Point2D minDistPt = null;
        boolean isTie = false;
        for (Point2D knownPoint : knownPoints) {
          if (knownPoint.equals(currPt)) {
            minDistPt = knownPoint;
            break;
          }

          int dist = GeomUtil.manhattanDist(currPt, knownPoint);
          if (dist < minDist) {
            minDist = dist;
            minDistPt = knownPoint;
            isTie = false;
          } else if (dist == minDist) {
            isTie = true;
          }
        }

        if (!isTie) {
          AdventUtil.incrementMap(areaCount, minDistPt);
        }
      }
    }

    return areaCount;
  }
}
