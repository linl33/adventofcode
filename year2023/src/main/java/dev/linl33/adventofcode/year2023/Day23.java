package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Stream;

public class Day23 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day23().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    var start = new Point2D(1, 0);
    var queue = new ArrayDeque<Point2D>();
    var visitedQueue = new ArrayDeque<List<Point2D>>();
    var pathLength = new HashMap<Point2D, Map<Point2D, Integer>>();

    queue.add(start);
    visitedQueue.add(new ArrayList<>());
    while (!queue.isEmpty()) {
      var next = queue.remove();
      var visited = visitedQueue.remove();

      var x = next.x();
      var y = next.y();

      var neighbors = new ArrayList<Point2D>();
      var inaccessible = new ArrayList<Point2D>();

      for (int delta = -1; delta <= 1; delta += 2) {
        var hPt = grid.getOrDefault(x + delta, y, '#');
        if (hPt != '#') {
          if ((delta < 0 && hPt != '>') || (delta > 0 && hPt != '<')) {
            neighbors.add(new Point2D(x + delta, y));
          } else {
            inaccessible.add(new Point2D(x + delta, y));
          }
        }

        var vPt = grid.getOrDefault(x, y + delta, '#');
        if (vPt != '#') {
          if ((delta < 0 && vPt != 'v') || (delta > 0 && vPt != '^')) {
            neighbors.add(new Point2D(x, y + delta));
          } else {
            inaccessible.add(new Point2D(x, y + delta));
          }
        }
      }

      pathLength.putIfAbsent(next, new HashMap<>());

      var visitedNeighbors = Stream.concat(neighbors.stream(), inaccessible.stream()).filter(visited::contains).toList();
      for (var visitedNeighbor : visitedNeighbors) {
        var neighborMaxPath = pathLength.get(visitedNeighbor).values().stream().max(Integer::compareTo).orElse(0);
        pathLength.get(next).put(visitedNeighbor, neighborMaxPath + 1);
      }

      neighbors.removeAll(visitedNeighbors);
      for (var neighbor : neighbors) {
        queue.add(neighbor);
        var visitedCopy = new ArrayList<>(visited);
        visitedCopy.add(next);
        visitedQueue.add(visitedCopy);
      }
    }

    var exit = new Point2D(width - 2, height - 1);
//    System.out.println(pathLength.get(exit));

    return pathLength.get(exit).values().iterator().next();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    var start = new Point2D(1, 0);
    var exit = new Point2D(width - 2, height - 1);
    var nodes = new ArrayList<Point2D>();
    nodes.add(start);
    nodes.add(exit);

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        var pt = grid.get(x, y);
        if (pt != '.') {
          continue;
        }

        var neighbors = 0;
        for (int delta = -1; delta <= 1; delta += 2) {
          if (grid.getOrDefault(x + delta, y, '#') != '#') {
            neighbors++;
          }
          if (grid.getOrDefault(x, y + delta, '#') != '#') {
            neighbors++;
          }
        }
        if (neighbors > 2) {
          nodes.add(new Point2D(x, y));
        }
      }
    }

    var edges = new HashMap<Point2D, Map<Point2D, Integer>>();
    nodes.forEach(n -> edges.put(n, new HashMap<>()));

    for (var node : nodes) {
      var nodeX = node.x();
      var nodeY = node.y();

      var queue = new ArrayDeque<Point2D>();
      var pathLength = new ArrayDeque<Integer>();
      var visited = new ArrayList<Point2D>();

      for (int delta = -1; delta <= 1; delta += 2) {
        if (grid.getOrDefault(nodeX + delta, nodeY, '#') != '#') {
          queue.add(new Point2D(nodeX + delta, nodeY));
          pathLength.add(1);
        }
        if (grid.getOrDefault(nodeX, nodeY + delta, '#') != '#') {
          queue.add(new Point2D(nodeX, nodeY + delta));
          pathLength.add(1);
        }
      }

      while (!queue.isEmpty()) {
        var next = queue.remove();
        int nextPathLength = pathLength.remove();

        if (next.equals(node)) {
          continue;
        }
        if (nodes.contains(next)) {
          edges.get(node).put(next, nextPathLength);
          continue;
        }
        if (visited.contains(next)) {
          continue;
        }

        for (int delta = -1; delta <= 1; delta += 2) {
          if (grid.getOrDefault(next.x() + delta, next.y(), '#') != '#') {
            queue.add(new Point2D(next.x() + delta, next.y()));
            pathLength.add(nextPathLength + 1);
          }
          if (grid.getOrDefault(next.x(), next.y() + delta, '#') != '#') {
            queue.add(new Point2D(next.x(), next.y() + delta));
            pathLength.add(nextPathLength + 1);
          }
        }

        visited.add(next);
      }
    }

    var queue = new ArrayDeque<Point2D>();
    var visitedQueue = new ArrayDeque<List<Point2D>>();

    queue.add(start);
    visitedQueue.add(new ArrayList<>());
    var m = -1;
    while (!queue.isEmpty()) {
      var next = queue.pop();
      var visited = visitedQueue.pop();
      visited.add(next);

      if (next.equals(exit)) {
        var pathLength = 0;
        var curr = start;
        for (int i = 1; i < visited.size(); i++) {
          var v = visited.get(i);
          pathLength += edges.get(curr).get(v);
          curr = v;
        }
        m = Math.max(m, pathLength);
        continue;
      }

      for (var kv : edges.get(next).entrySet()) {
        if (visited.contains(kv.getKey())) {
          continue;
        }

        queue.add(kv.getKey());
        visitedQueue.add(new ArrayList<>(visited));
      }
    }

    return m;
  }
}
