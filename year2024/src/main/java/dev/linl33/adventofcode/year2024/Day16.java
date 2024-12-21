package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class Day16 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
//    new Day16().runAndPrintAll();

//    new Day16().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day16test1"));
//    new Day16().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day16"));
//    new Day16().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day16test2"));
    new Day16().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day16"));
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);
    var dim = lines.length;

    Point2D start = null;
    Point2D end = null;

    for (var y = 0; y < dim; y++) {
      for (var x = 0; x < dim; x++) {
        if (lines[y].charAt(x) == 'S') {
          start = new Point2D(x, y);
        } else if (lines[y].charAt(x) == 'E') {
          end = new Point2D(x, y);
        }
      }
    }

    if (start == null || end == null) {
      throw new IllegalStateException("Start or end point not found");
    }

    var dist = new int[dim * dim * 4];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[start.y() * dim + start.x() << 2 | 3] = 0;

    var buckets = new ArrayDeque<ArrayDeque<Integer>>();
    var nextBucket = new ArrayDeque<Integer>();

    nextBucket.add(start.y() * dim + start.x() << 2 | 3);

    while (!nextBucket.isEmpty() && !buckets.isEmpty()) {
      if (nextBucket.isEmpty()) {
        nextBucket = buckets.removeFirst();
      }

      var bucketSize = nextBucket.size();
      for (var i = 0; i < bucketSize; i++) {
        var next = nextBucket.removeFirst();

        var pt = next >> 2;
        var dir = next & 0b11;
      }
    }

    var minCost = Integer.MAX_VALUE;

//    for (int direction = 0; direction < 4; direction++) {
//      var path = GraphUtil.aStar(
//        new Node(start, 3),
//        new Node(end, direction),
//        (Node node) -> {
//          var dirClockwise = (node.direction + 1) % 4;
//          var dirCounterClockwise = (node.direction + 3) % 4;
//
//          Point2D vector;
//          if (node.direction == 0) {
//            vector = new Point2D(0, -1);
//          } else if (node.direction == 1) {
//            vector = new Point2D(1, 0);
//          } else if (node.direction == 2) {
//            vector = new Point2D(0, 1);
//          } else {
//            vector = new Point2D(-1, 0);
//          }
//
//          var next = node.point.translate(vector);
//
//          var neighbors = new ArrayList<Node>();
//          if (next.x() >= 0 && next.x() < dim && next.y() >= 0 && next.y() < dim && lines[next.y()].charAt(next.x()) != '#') {
//            neighbors.add(new Node(next, node.direction));
//          }
//
//          neighbors.add(new Node(node.point, dirClockwise));
//          neighbors.add(new Node(node.point, dirCounterClockwise));
//
//          return neighbors;
//        },
//        (Node _) -> 0,
//        (Node left, Node right) -> left.direction == right.direction ? 1 : 1000
//      ).orElseThrow();
//
//      var cost = path.length();
//      minCost = Math.min(minCost, cost);
//    }

    return minCost;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);
    var dim = lines.length;

    Point2D start = null;
    Point2D end = null;

    for (var y = 0; y < dim; y++) {
      for (var x = 0; x < dim; x++) {
        if (lines[y].charAt(x) == 'S') {
          start = new Point2D(x, y);
        } else if (lines[y].charAt(x) == 'E') {
          end = new Point2D(x, y);
        }
      }
    }

    if (start == null || end == null) {
      throw new IllegalStateException("Start or end point not found");
    }

    var q = new ArrayDeque<Node>();
    q.add(new Node(start, 3));

    var paths = new ArrayDeque<List<Node>>();
    paths.add(new ArrayList<>());

    var costs = new ArrayDeque<Integer>();
    costs.add(0);

    var nodes = new HashMap<Integer, HashSet<Point2D>>();

    var minCost = Integer.MAX_VALUE;

//    while (!q.isEmpty()) {
//      var node = q.removeFirst();
//      var path = paths.removeFirst();
//      int cost = costs.removeFirst();
//
//      if (cost > minCost) {
//        continue;
//      }
//
//      if (node.point.equals(end)) {
//        minCost = cost;
//
//        nodes.putIfAbsent(cost, new HashSet<>());
//        var set = nodes.get(cost);
//
//        path.forEach(n -> set.add(n.point));
//        continue;
//      }
//
//      var dirClockwise = (node.direction + 1) % 4;
//      var dirCounterClockwise = (node.direction + 3) % 4;
//
//      Point2D vector;
//      if (node.direction == 0) {
//        vector = new Point2D(0, -1);
//      } else if (node.direction == 1) {
//        vector = new Point2D(1, 0);
//      } else if (node.direction == 2) {
//        vector = new Point2D(0, 1);
//      } else {
//        vector = new Point2D(-1, 0);
//      }
//
//      var next = node.point.translate(vector);
//
//      var neighbors = new ArrayList<Node>();
//      if (next.x() >= 0 && next.x() < dim && next.y() >= 0 && next.y() < dim && lines[next.y()].charAt(next.x()) != '#') {
////        neighbors.add(new Node(next, node.direction));
//
//        if (!path.contains(new Node(next, node.direction))) {
//          q.addFirst(new Node(next, node.direction));
//
//          var newPath = new ArrayList<>(path);
//          newPath.add(new Node(next, node.direction));
//          paths.addFirst(newPath);
//
//          costs.addFirst(cost + 1);
//        }
//      }
//
//      neighbors.add(new Node(node.point, dirClockwise));
//      neighbors.add(new Node(node.point, dirCounterClockwise));
//
//      neighbors.forEach(n -> {
//        if (!path.contains(n)) {
//          q.addFirst(n);
//
//          var newPath = new ArrayList<>(path);
//          newPath.add(n);
//          paths.addFirst(newPath);
//
//          costs.addFirst(cost + 1000);
//        }
//      });
//
////      neighbors.forEach(q::addFirst);
//    }

//    var minCost = Integer.MAX_VALUE;

    var endFinal = end;
    var dst = new Node(end, -1);

    var path = GraphUtil.aStar(
      new Node(start, 3),
      dst,
      (Node node) -> {
        var dirClockwise = (node.direction + 1) % 4;
        var dirCounterClockwise = (node.direction + 3) % 4;

        Point2D vector;
        if (node.direction == 0) {
          vector = new Point2D(0, -1);
        } else if (node.direction == 1) {
          vector = new Point2D(1, 0);
        } else if (node.direction == 2) {
          vector = new Point2D(0, 1);
        } else {
          vector = new Point2D(-1, 0);
        }

        var next = node.point.translate(vector);
        if (next.equals(endFinal)) {
          return List.of(dst);
        }

        var neighbors = new ArrayList<Node>();
        if (next.x() >= 0 && next.x() < dim && next.y() >= 0 && next.y() < dim && lines[next.y()].charAt(next.x()) != '#') {
          neighbors.add(new Node(next, node.direction));
        }

        neighbors.add(new Node(node.point, dirClockwise));
        neighbors.add(new Node(node.point, dirCounterClockwise));

        return neighbors;
      },
      (Node _) -> 0,
      (Node left, Node right) -> left.point.equals(right.point) ? 1000 : 1
    ).orElseThrow();

    minCost = path.length();
    System.out.println(minCost);

    nodes.putIfAbsent(minCost, new HashSet<>());

    System.out.println(path.path().get(path.start()));
    System.out.println(path.path().get(path.end()));

    var removeQ = new ArrayDeque<>(path.path().values());
    removeQ.remove(path.end());

    var removeVisited = new HashSet<Node>();
    while (!removeQ.isEmpty()) {
      System.out.println(removeQ.size());

      var curr = removeQ.removeFirst();
      var toRemove = Set.of(curr);
      removeVisited.add(curr);

      var path2 = GraphUtil.aStar(
        new Node(start, 3),
        dst,
        (Node node) -> {
          var dirClockwise = (node.direction + 1) % 4;
          var dirCounterClockwise = (node.direction + 3) % 4;

          Point2D vector;
          if (node.direction == 0) {
            vector = new Point2D(0, -1);
          } else if (node.direction == 1) {
            vector = new Point2D(1, 0);
          } else if (node.direction == 2) {
            vector = new Point2D(0, 1);
          } else {
            vector = new Point2D(-1, 0);
          }

          var next = node.point.translate(vector);
          if (next.equals(endFinal)) {
            return List.of(dst);
          }

          var neighbors = new ArrayList<Node>();
          if (next.x() >= 0 && next.x() < dim && next.y() >= 0 && next.y() < dim && lines[next.y()].charAt(next.x()) != '#') {
            neighbors.add(new Node(next, node.direction));
          }

          neighbors.add(new Node(node.point, dirClockwise));
          neighbors.add(new Node(node.point, dirCounterClockwise));

          neighbors.removeIf(toRemove::contains);

          return neighbors;
        },
        (Node n) -> n.point.manhattanDistance(dst.point),
        (Node left, Node right) -> left.point.equals(right.point) ? 1000 : 1
      );

      if (path2.isEmpty()) {
        continue;
      }

      var cost = path2.orElseThrow().length();
      if (cost < minCost) {
        throw new IllegalStateException("Cost is less than min cost");
      }

      if (cost == minCost) {
        nodes.putIfAbsent(minCost, new HashSet<>());

        var v = nodes.get(minCost);
        path2.orElseThrow().path().keySet().forEach(n -> v.add(n.point));
        path2.orElseThrow().path().values().forEach(n -> v.add(n.point));

        path2.orElseThrow().path().keySet().forEach(n -> {
          if (!removeVisited.contains(n)) {
            removeVisited.add(n);
            removeQ.addLast(n);
          }
        });
        path2.orElseThrow().path().values().forEach(n -> {
          if (!removeVisited.contains(n)) {
            removeVisited.add(n);
            removeQ.addLast(n);
          }
        });
      }
    }

//    for (int y = 0; y < dim; y++) {
//      for (int x = 0; x < dim; x++) {
//        System.out.println(dim * dim - (y * dim + x));
//
//        var val = lines[y].charAt(x);
//        if (val == '#') {
//          continue;
//        }
//
//        var waypt = new Point2D(x, y);
//        if (nodes.get(minCost).contains(waypt)) {
//          continue;
//        }
//
//        var estDistance = start.manhattanDistance(waypt) + waypt.manhattanDistance(end);
//        if (estDistance > minCost) {
//          continue;
//        }
//
//        for (int wayDir = 0; wayDir < 4; wayDir++) {
//          var wayptNode = new Node(waypt, wayDir);
//
//          var path1 = GraphUtil.aStar(
//            new Node(start, 3),
//            wayptNode,
//            (Node node) -> {
//              var dirClockwise = (node.direction + 1) % 4;
//              var dirCounterClockwise = (node.direction + 3) % 4;
//
//              Point2D vector;
//              if (node.direction == 0) {
//                vector = new Point2D(0, -1);
//              } else if (node.direction == 1) {
//                vector = new Point2D(1, 0);
//              } else if (node.direction == 2) {
//                vector = new Point2D(0, 1);
//              } else {
//                vector = new Point2D(-1, 0);
//              }
//
//              var next = node.point.translate(vector);
//
////              if (next.equals(waypt)) {
////                return List.of(wayptNode);
////              }
//
//              var neighbors = new ArrayList<Node>();
//              if (next.x() >= 0 && next.x() < dim && next.y() >= 0 && next.y() < dim && lines[next.y()].charAt(next.x()) != '#') {
//                neighbors.add(new Node(next, node.direction));
//              }
//
//              neighbors.add(new Node(node.point, dirClockwise));
//              neighbors.add(new Node(node.point, dirCounterClockwise));
//
//              return neighbors;
//            },
//            (Node n) -> n.point.manhattanDistance(waypt),
//            (Node left, Node right) -> left.point.equals(right.point) ? 1000 : 1
//          );
//
//          if (path1.isEmpty()) {
//            continue;
//          }
//
//          estDistance = path1.orElseThrow().length() + waypt.manhattanDistance(end);
//          if (estDistance > minCost) {
//            continue;
//          }
//
//          var path2 = GraphUtil.aStar(
//            wayptNode,
//            dst,
//            (Node node) -> {
//              var dirClockwise = (node.direction + 1) % 4;
//              var dirCounterClockwise = (node.direction + 3) % 4;
//
//              Point2D vector;
//              if (node.direction == 0) {
//                vector = new Point2D(0, -1);
//              } else if (node.direction == 1) {
//                vector = new Point2D(1, 0);
//              } else if (node.direction == 2) {
//                vector = new Point2D(0, 1);
//              } else {
//                vector = new Point2D(-1, 0);
//              }
//
//              var next = node.point.translate(vector);
//              if (next.equals(endFinal)) {
//                return List.of(dst);
//              }
//
//              var neighbors = new ArrayList<Node>();
//              if (next.x() >= 0 && next.x() < dim && next.y() >= 0 && next.y() < dim && lines[next.y()].charAt(next.x()) != '#') {
//                neighbors.add(new Node(next, node.direction));
//              }
//
//              neighbors.add(new Node(node.point, dirClockwise));
//              neighbors.add(new Node(node.point, dirCounterClockwise));
//
//              return neighbors;
//            },
//            (Node n) -> n.point.manhattanDistance(dst.point),
//            (Node left, Node right) -> left.point.equals(right.point) ? 1000 : 1
//          );
//
//          if (path2.isEmpty()) {
//            continue;
//          }
//
//          var cost = path1.orElseThrow().length() + path2.orElseThrow().length();
////        minCost = Math.min(minCost, cost);
//
//          if (cost < minCost) {
//            throw new IllegalStateException("Cost is less than min cost");
//          }
//
//          if (cost == minCost) {
//            nodes.putIfAbsent(minCost, new HashSet<>());
//
//            var v = nodes.get(minCost);
//            path1.orElseThrow().path().keySet().forEach(n -> v.add(n.point));
//            path1.orElseThrow().path().values().forEach(n -> v.add(n.point));
//
//            path2.orElseThrow().path().keySet().forEach(n -> v.add(n.point));
//            path2.orElseThrow().path().values().forEach(n -> v.add(n.point));
//          }
//        }
//      }
//    }

    return nodes.get(minCost).size();
  }

  private record Node(Point2D point, int direction) {
  }
}
