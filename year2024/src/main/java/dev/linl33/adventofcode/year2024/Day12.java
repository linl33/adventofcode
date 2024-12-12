package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class Day12 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
    new Day12().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var dim = lines.length;

    var map = new HashMap<Point2D, Integer>();
    var regions = new HashMap<Point2D, Integer>();

    for (var y = 0; y < dim; y++) {
      for (var x = 0; x < dim; x++) {
        map.put(new Point2D(x, y), lines[y].codePointAt(x));
      }
    }

    var result = 0;

    for (var y = 0; y < dim; y++) {
      for (var x = 0; x < dim; x++) {
        var pt = new Point2D(x, y);
        if (regions.containsKey(pt))  {
          continue;
        }

        var regionId = regions.size();
        regions.put(pt, regionId);

        var q = new ArrayDeque<Point2D>();
        q.add(pt);

        var region = new ArrayList<Point2D>();
        region.add(pt);

        while (!q.isEmpty()) {
          var curr = q.removeFirst();

          var up = curr.translate(0, -1);
          var down = curr.translate(0, 1);
          var left = curr.translate(-1, 0);
          var right = curr.translate(1, 0);

          var neighbors = List.of(up, down, left, right);
          for (var neighbor : neighbors) {
            if (!map.containsKey(neighbor)) {
              continue;
            }

            if (regions.containsKey(neighbor)) {
              continue;
            }

            if (map.get(curr).equals(map.get(neighbor))) {
              regions.put(neighbor, regionId);
              region.add(neighbor);
              q.add(neighbor);
            }
          }
        }

        var totalPerimeter = 0;
        for (var curr : region) {
          var perimeter = 4;

          var up = curr.translate(0, -1);
          var down = curr.translate(0, 1);
          var left = curr.translate(-1, 0);
          var right = curr.translate(1, 0);

          var neighbors = List.of(up, down, left, right);
          for (var neighbor : neighbors) {
            if (regions.containsKey(neighbor) && regions.get(neighbor).equals(regionId)) {
              perimeter--;
            }
          }

          totalPerimeter += perimeter;
        }

        result += region.size() * totalPerimeter;
      }
    }

    return result;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var dim = lines.length;

    var map = new HashMap<Point2D, Integer>();
    var regions = new HashMap<Point2D, Integer>();

    for (var y = 0; y < dim; y++) {
      for (var x = 0; x < dim; x++) {
        map.put(new Point2D(x, y), lines[y].codePointAt(x));
      }
    }

    var result = 0;

    for (var y = 0; y < dim; y++) {
      for (var x = 0; x < dim; x++) {
        var pt = new Point2D(x, y);
        if (regions.containsKey(pt))  {
          continue;
        }

        var regionId = regions.size();
        regions.put(pt, regionId);

        var q = new ArrayDeque<Point2D>();
        q.add(pt);

        var region = new ArrayList<Point2D>();
        region.add(pt);

        while (!q.isEmpty()) {
          var curr = q.removeFirst();

          var up = curr.translate(0, -1);
          var down = curr.translate(0, 1);
          var left = curr.translate(-1, 0);
          var right = curr.translate(1, 0);

          var neighbors = List.of(up, down, left, right);
          for (var neighbor : neighbors) {
            if (!map.containsKey(neighbor)) {
              continue;
            }

            if (regions.containsKey(neighbor)) {
              continue;
            }

            if (map.get(curr).equals(map.get(neighbor))) {
              regions.put(neighbor, regionId);
              region.add(neighbor);
              q.add(neighbor);
            }
          }
        }

        var regionEdges = new HashSet<Edge>();
        var edgeOwner = new HashMap<Edge, Point2D>();

        for (var curr : region) {
          var v = new DoublePoint2d[] {
            new DoublePoint2d(curr.x() - 0.5, curr.y() - 0.5),
            new DoublePoint2d(curr.x() + 0.5, curr.y() - 0.5),
            new DoublePoint2d(curr.x() + 0.5, curr.y() + 0.5),
            new DoublePoint2d(curr.x() - 0.5, curr.y() + 0.5),
          };

          var edges = Set.of(
            new Edge(v[0], v[1]),
            new Edge(v[1], v[2]),
            new Edge(v[2], v[3]),
            new Edge(v[3], v[0])
          );

          edges.forEach(edge -> edgeOwner.put(edge, curr));

          var newEdges = new HashSet<>(edges);
          newEdges.removeAll(regionEdges);

          regionEdges.removeAll(edges);
          regionEdges.addAll(newEdges);
        }

        var sides = regionEdges.size();
        var edgesArr = regionEdges.toArray(new Edge[0]);
        for (var i = 0; i < edgesArr.length; i++) {
          var edge = edgesArr[i];
          var owner = edgeOwner.get(edge);

          for (var j = i + 1; j < edgesArr.length; j++) {
            var otherEdge = edgesArr[j];
            var otherOwner = edgeOwner.get(otherEdge);

            if (owner.x() == otherOwner.x() || owner.y() == otherOwner.y()) {
              if (edge.start.equals(otherEdge.start) || edge.start.equals(otherEdge.end) || edge.end.equals(otherEdge.start) || edge.end.equals(otherEdge.end)) {
                if (new HashSet<>(List.of(edge.start.x, edge.end.x, otherEdge.start.x, otherEdge.end.x)).size() == 1) {
                  sides--;
                }

                if (new HashSet<>(List.of(edge.start.y, edge.end.y, otherEdge.start.y, otherEdge.end.y)).size() == 1) {
                  sides--;
                }
              }
            }
          }
        }

        result += region.size() * sides;
      }
    }

    return result;
  }

  private record DoublePoint2d(double x, double y) {
    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;

      DoublePoint2d that = (DoublePoint2d) o;
      return Double.compare(x, that.x) == 0 && Double.compare(y, that.y) == 0;
    }

    @Override
    public int hashCode() {
      int result = Double.hashCode(x);
      result = 31 * result + Double.hashCode(y);
      return result;
    }
  }

  private record Edge(DoublePoint2d start, DoublePoint2d end) {
    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;

      Edge edge = (Edge) o;
      return (Objects.equals(end, edge.end) || Objects.equals(end, edge.start)) && (Objects.equals(start, edge.end) || Objects.equals(start, edge.start));
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(start) ^ Objects.hashCode(end);
    }
  }
}
