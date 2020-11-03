package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.point.Point;
import dev.linl33.adventofcode.lib.point.Point2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day3 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
    new Day3().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws IOException {
    var wire1 = parseInput(reader.readLine());
    var wire2 = parseInput(reader.readLine());

    return findCollisions(wire1, wire2)
        .stream()
        .map(Point.ORIGIN_2D::manhattanDistance)
        .min(Comparator.naturalOrder())
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public Integer part2(BufferedReader reader) throws IOException {
    var wire1 = parseInput(reader.readLine());
    var wire2 = parseInput(reader.readLine());

    return findCollisions(wire1, wire2)
        .stream()
        .map(pt -> distToPt(wire1, pt) + distToPt(wire2, pt))
        .min(Comparator.naturalOrder())
        .orElseThrow(IllegalArgumentException::new);
  }

  private static HashSet<Point2D> tracePath(List<WireMovement> movements) {
    var pathPts = new HashSet<Point2D>();

    var curr = new Point2D(0, 0);
    for (WireMovement movement : movements) {
      for (int i = 1; i <= movement.magnitude; i++) {
        curr = movement.nextPos(curr);
        pathPts.add(curr);
      }
    }

    pathPts.remove(Point.ORIGIN_2D);
    return pathPts;
  }

  private static int distToPt(List<WireMovement> movements, Point2D target) {
    var steps = 0;

    var curr = new Point2D(0, 0);
    for (WireMovement movement : movements) {
      for (int i = 1; i <= movement.magnitude; i++) {
        curr = movement.nextPos(curr);
        steps++;

        if (curr.equals(target)) {
          return steps;
        }
      }
    }

    throw new IllegalArgumentException();
  }

  private static Set<Point2D> findCollisions(List<WireMovement> wire1, List<WireMovement> wire2) {
    var path1 = tracePath(wire1);
    var path2 = tracePath(wire2);
    path1.retainAll(path2);

    return path1;
  }

  private static List<WireMovement> parseInput(String serialized) {
    return Arrays
        .stream(serialized.split(","))
        .map(WireMovement::new)
        .collect(Collectors.toList());
  }

  private static class WireMovement {
    public enum Direction {
      U, D, L, R
    }

    private final int magnitude;
    private final int deltaX;
    private final int deltaY;

    public WireMovement(Direction direction, int magnitude) {
      this.magnitude = magnitude;

      var tmpX = 0;
      var tmpY = 0;
      switch (direction) {
        case U -> tmpY = 1;
        case D -> tmpY = -1;
        case L -> tmpX = 1;
        case R -> tmpX = -1;
      }
      this.deltaX = tmpX;
      this.deltaY = tmpY;
    }

    public WireMovement(String movement) {
      this(
          Direction.valueOf(movement.substring(0, 1)),
          Integer.parseInt(movement.substring(1))
      );
    }

    public Point2D nextPos(Point2D currPos) {
      return currPos.translate(deltaX, deltaY);
    }
  }
}
