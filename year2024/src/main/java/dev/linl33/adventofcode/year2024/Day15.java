package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.point.Point;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.text.CompactNumberFormat;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;

public class Day15 extends AdventSolution2024<Integer, Integer> {
  public static void main() {
//    new Day15().runAndPrintAll();

//    new Day15().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day15test1"));
//    new Day15().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day15test2"));
//    new Day15().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day15"));
//    new Day15().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day15test3"));
//    new Day15().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day15test2"));
    new Day15().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day15"));
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).map(x -> x.toArray(String[]::new)).toArray(String[][]::new);

    var grid = groups[0];
    var movements = String.join("", groups[1]);

    var dim = grid.length;

    var boxes = new HashSet<Point2D>();
    var walls = new HashSet<Point2D>();

    var curr = new Point2D(0, 0);

    for (int y = 0; y < dim; y++) {
      for (int x = 0; x < dim; x++) {
        var val = grid[y].charAt(x);
        if (val == 'O') {
          boxes.add(new Point2D(x, y));
        } else if (val == '#') {
          walls.add(new Point2D(x, y));
        } else if (val == '@') {
          curr = new Point2D(x, y);
        }
      }
    }

    for (var i = 0; i < movements.length(); i++) {
      var val = movements.charAt(i);

      Point2D delta;
      if (val == '^') {
        delta = new Point2D(0, -1);
      } else if (val == 'v') {
        delta = new Point2D(0, 1);
      } else if (val == '<') {
        delta = new Point2D(-1, 0);
      } else if (val == '>') {
        delta = new Point2D(1, 0);
      } else {
        throw new IllegalArgumentException();
      }

      var next = curr.translate(delta);

      if (walls.contains(next)) {
        continue;
      } else if (boxes.contains(next)) {
        pushRecursive(next, delta, boxes, walls);
        if (boxes.contains(next)) {
          next = curr;
        }
      }

      curr = next;
    }

    var sum = 0;

    for (var box : boxes) {
      sum += box.x() + box.y() * 100;
    }

    return sum;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var groups = AdventUtil.readInputGrouped(reader).map(x -> x.toArray(String[]::new)).toArray(String[][]::new);

    var grid = groups[0];
    var movements = String.join("", groups[1]);

    var dim = grid.length;

    var boxesLeft = new HashSet<Point2D>();
    var boxesRight = new HashSet<Point2D>();
    var walls = new HashSet<Point2D>();

    var curr = new Point2D(0, 0);

    for (int y = 0; y < dim; y++) {
      for (int x = 0; x < dim; x++) {
        var val = grid[y].charAt(x);
        if (val == 'O') {
          boxesLeft.add(new Point2D(x * 2, y));
          boxesRight.add(new Point2D(x * 2 + 1, y));
        } else if (val == '#') {
          walls.add(new Point2D(x * 2, y));
          walls.add(new Point2D(x * 2 + 1, y));
        } else if (val == '@') {
          curr = new Point2D(x * 2, y);
        }
      }
    }

//    System.out.println(boxesLeft.size());
//    System.out.println(boxesLeft);

    for (var i = 0; i < movements.length(); i++) {
      var val = movements.charAt(i);
//      System.out.println(val);

      Point2D delta;
      if (val == '^') {
        delta = new Point2D(0, -1);
      } else if (val == 'v') {
        delta = new Point2D(0, 1);
      } else if (val == '<') {
        delta = new Point2D(-1, 0);
      } else if (val == '>') {
        delta = new Point2D(1, 0);
      } else {
        throw new IllegalArgumentException();
      }

      var next = curr.translate(delta);

      if (walls.contains(next)) {
        next = curr;
      } else if (boxesLeft.contains(next) || boxesRight.contains(next)) {
        pushRecursive(next, delta, boxesLeft, boxesRight, walls);
        if (boxesLeft.contains(next) || boxesRight.contains(next)) {
          next = curr;
        }
      }

      curr = next;

//      System.out.println(boxesLeft);
    }

    var sum = 0;

    for (var box : boxesLeft) {
      sum += box.x() + box.y() * 100;
    }

//    System.out.println(boxesLeft.size());
//    System.out.println(boxesRight.size());
//    System.out.println(boxesLeft);
//    System.out.println(sum);

    return sum;
  }

  private static void pushRecursive(Point2D toPush, Point2D delta, HashSet<Point2D> boxes, HashSet<Point2D> walls) {
    if (!boxes.contains(toPush)) {
      throw new IllegalStateException();
    }

    var next = toPush.translate(delta);

    if (walls.contains(next)) {
      return;
    }

    // empty space
    if (!boxes.contains(next)) {
      boxes.remove(toPush);
      boxes.add(next);
      return;
    }

    pushRecursive(next, delta, boxes, walls);
    // empty space
    if (!boxes.contains(next)) {
      boxes.remove(toPush);
      boxes.add(next);
    }
  }

  private static void pushRecursive(Point2D toPush, Point2D delta, HashSet<Point2D> boxesLeft, HashSet<Point2D> boxesRight, HashSet<Point2D> walls) {
//    if (!boxesLeft.contains(toPush) && !boxesRight.contains(toPush)) {
//      throw new IllegalStateException();
//    }

//    boxesLeft.forEach(pt -> {
//      if (boxesRight.contains(pt)) {
//        throw new IllegalStateException();
//      }
//    });
//
//    boxesRight.forEach(pt -> {
//      if (boxesLeft.contains(pt)) {
//        throw new IllegalStateException();
//      }
//    });

    if (delta.y() == 0) {
      var next = toPush.translate(delta);

      if (walls.contains(next)) {
        return;
      }

      // empty space
      if (!boxesLeft.contains(next) && !boxesRight.contains(next)) {
        if (boxesLeft.contains(toPush)) {
          boxesLeft.remove(toPush);
          boxesLeft.add(next);
        } else {
          boxesRight.remove(toPush);
          boxesRight.add(next);
        }
        return;
      }

      pushRecursive(next, delta, boxesLeft, boxesRight, walls);
      // empty space
      if (!boxesLeft.contains(next) && !boxesRight.contains(next)) {
        if (boxesLeft.contains(toPush)) {
          boxesLeft.remove(toPush);
          boxesLeft.add(next);
        } else {
          boxesRight.remove(toPush);
          boxesRight.add(next);
        }
      }
    }

    if (delta.x() == 0) {
      var toPushLeft = boxesLeft.contains(toPush) ? toPush : toPush.translate(-1, 0);
      var toPushRight = boxesRight.contains(toPush) ? toPush : toPush.translate(1, 0);

      var nextLeft = toPushLeft.translate(delta);
      var nextRight = toPushRight.translate(delta);

      if (walls.contains(nextLeft) || walls.contains(nextRight)) {
        return;
      }

      // empty space
      if (!boxesLeft.contains(nextLeft) && !boxesLeft.contains(nextRight) && !boxesRight.contains(nextLeft) && !boxesRight.contains(nextRight)) {
        boxesLeft.remove(toPushLeft);
        boxesLeft.add(nextLeft);

        boxesRight.remove(toPushRight);
        boxesRight.add(nextRight);

        return;
      }

      var reachable = new HashSet<Point2D>();
      var reachableLeft = new HashSet<Point2D>();
      var reachableRight = new HashSet<Point2D>();

      var q = new ArrayDeque<Point2D>();

      q.add(toPushLeft);
      q.add(toPushRight);

      while (!q.isEmpty()) {
        var curr = q.removeFirst();

        var currLeft = boxesLeft.contains(curr) ? curr : curr.translate(-1, 0);
        var currRight = boxesRight.contains(curr) ? curr : curr.translate(1, 0);

        if (reachable.contains(currLeft) && reachable.contains(currRight)) {
          continue;
        }

        reachable.add(currLeft);
        reachable.add(currRight);

        reachableLeft.add(currLeft);
        reachableRight.add(currRight);

        var nLeft = currLeft.translate(delta);
        var nRight = currRight.translate(delta);

        if (boxesLeft.contains(nLeft) || boxesRight.contains(nLeft)) {
          q.addLast(nLeft);
        }

        if (boxesLeft.contains(nRight) || boxesRight.contains(nRight)) {
          q.addLast(nRight);
        }
      }

//      if (reachable.size() % 2 != 0) {
//        throw new IllegalStateException();
//      }
//
//      if (reachable.size() != reachableLeft.size() + reachableRight.size()) {
//        throw new IllegalStateException();
//      }

      var moveable = true;
      for (var r : reachable) {
        if (walls.contains(r.translate(delta))) {
          moveable = false;
          break;
        }
//
//        var n = r.translate(delta);
//        if (boxesLeft.contains(n) || boxesRight.contains(n)) {
//          if (!reachableLeft.contains(n) && !reachableRight.contains(n)) {
//            throw new IllegalStateException();
//          }
//        }
      }

      if (moveable) {
//        if (boxesLeft.size() != boxesRight.size()) {
//          throw new IllegalStateException();
//        }

//        reachableLeft.forEach(pt -> {
//          if (boxesLeft.contains(pt.translate(delta)) && !reachableLeft.contains(pt.translate(delta))) {
//            throw new IllegalStateException();
//          }
//        });
//        reachableRight.forEach(pt -> {
//          if (boxesRight.contains(pt.translate(delta)) && !reachableRight.contains(pt.translate(delta))) {
//            throw new IllegalStateException();
//          }
//        });

        var sizeLeft = boxesLeft.size();
        boxesLeft.removeAll(reachableLeft);

        var sizeRight = boxesRight.size();
        boxesRight.removeAll(reachableRight);

        var leftToAdd = reachableLeft.stream().map(p -> p.translate(delta)).toList();
        var rightToAdd = reachableRight.stream().map(p -> p.translate(delta)).toList();

//        leftToAdd.forEach(pt -> {
//          if (boxesRight.contains(pt)) {
//            throw new IllegalStateException();
//          }
//        });
//
//        reachableRight.forEach(pt -> {
//          var n = pt.translate(delta);
//          if (boxesLeft.contains(n)) {
//            System.out.println(reachableLeft.contains(n));
//            System.out.println(reachableLeft.contains(pt));
//            throw new IllegalStateException();
//          }
//        });

        boxesLeft.addAll(leftToAdd);
//        if (boxesLeft.size() != sizeLeft) {
//          throw new IllegalStateException();
//        }

        boxesRight.addAll(rightToAdd);
//        if (boxesRight.size() != sizeRight) {
//          throw new IllegalStateException();
//        }

//        boxesLeft.forEach(pt -> {
//          if (boxesRight.contains(pt)) {
//            throw new IllegalStateException();
//          }
//        });
//
//        boxesRight.forEach(pt -> {
//          if (boxesLeft.contains(pt)) {
//            throw new IllegalStateException();
//          }
//        });
      }
    }
  }
}
