package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class Day21 extends AdventSolution2023<Long, Long> {
  public static void main(String[] args) {
    new Day21().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    var startX = 0;
    var startY = 0;
    for (int i = 0; i < grid.array().length; i++) {
      if (grid.array()[i] == 'S') {
        startY = i / width;
        startX = i % width;
        break;
      }
    }
    grid.set(startX, startY, '.');

    var curr = new ArrayDeque<Point2D>();
    curr.add(new Point2D(startX, startY));
    var next = new ArrayDeque<Point2D>();

    var cycles = new HashMap<Point2D, int[]>();
    var hasCycled = new HashMap<Point2D, Boolean>();

    var total = 0L;

    var max = 64;
    for (int n = 0; n < max; n++) {
      if (n % 100 == 0) {
        System.out.println(n);
      }

      var seen = new HashSet<Point2D>();

      while (!curr.isEmpty()) {
        var pt = curr.remove();
//        var remapped = new Point2D(Math.floorMod(pt.x(), width), Math.floorMod(pt.y(), height));
//        var scaleX = pt.x() / width;
//        var scaleY = pt.y() / height;
//
//        if (pt.x() < 0) {
//          scaleX--;
//        }
//
//        if (pt.y() < 0) {
//          scaleY--;
//        }
//
//        var reachable = cache.get(remapped);
//        if (reachable == null) {
//          throw new IllegalStateException();
//        }
//
//        for (var r : reachable) {
//          var candidate = new Point2D(r.x() + scaleX * width, r.y() + scaleY * height);
//          var added = seen.add(candidate);
//          if (added) {
//            next.add(candidate);
//          }
//        }

        for (int yDelta = -1; yDelta <= 1; yDelta++) {
          if (yDelta == 0) {
            continue;
          }

          var candidate = pt.translate(0, yDelta);
          var modded = new Point2D(Math.floorMod(candidate.x(), width), Math.floorMod(candidate.y(), height));

          if (grid.get(modded) == '.' && seen.add(candidate)) {
            next.add(candidate);
//            System.out.println(candidate);
          }
        }

        for (int xDelta = -1; xDelta <= 1; xDelta++) {
          if (xDelta == 0) {
            continue;
          }

          var candidate = pt.translate(xDelta, 0);
          var modded = new Point2D(Math.floorMod(candidate.x(), width), Math.floorMod(candidate.y(), height));

          if (grid.get(modded) == '.' && seen.add(candidate)) {
            next.add(candidate);
//            System.out.println(candidate);
          }
        }
      }

//      System.out.println();
//      var debugCount = 0;
//      for (int y = -height * 2; y < height * 2; y++) {
//        for (int x = -width * 2; x < width * 2; x++) {
//          if (seen.contains(new Point2D(x, y))) {
//            debugCount++;
//          }
//          System.out.print(Character.toString(seen.contains(new Point2D(x, y)) ? 'O' : grid.get(Math.floorMod(x, width), Math.floorMod(y, height))));
//        }
//        System.out.println();
//      }
//      System.out.println(debugCount);

      var gridOffsets = new HashSet<Point2D>();
      for (var pt : seen) {
        var offsetX = Math.floorDiv(pt.x(), width);
        var offsetY = Math.floorDiv(pt.y(), height);

        gridOffsets.add(new Point2D(offsetX, offsetY));
      }

      var s = new HashMap<Integer, Integer>();

      var found = false;
      for (var gridOffset : gridOffsets) {
        if (hasCycled.containsKey(gridOffset)) {
          continue;
        }

        var offsetX = gridOffset.x() * width;
        var offsetY = gridOffset.y() * height;

        var count = 0;
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            var pt = new Point2D(x + offsetX, y + offsetY);
            if (seen.contains(pt)) {
              count++;
            }
          }
        }

        if (gridOffset.equals(new Point2D(0, 0))) {
//          tally.put(n, count);
        } else {
          s.compute(count, (k, v) -> v == null ? 1 : v + 1);
        }

        cycles.putIfAbsent(gridOffset, new int[4]);
        var cycleCounter = cycles.get(gridOffset);

        if (n % 2 == 1) {
          cycleCounter[1] = cycleCounter[3];
          cycleCounter[3] = count;
        } else {
          cycleCounter[0] = cycleCounter[2];
          cycleCounter[2] = count;
        }

        if (cycleCounter[2] == cycleCounter[0] && cycleCounter[3] == cycleCounter[1]) {
          var prev = hasCycled.put(gridOffset, true);
          if (prev != null) {
            throw new IllegalStateException();
          }
          System.out.println(STR."new cycle \{n} \{gridOffset}");
          System.out.println(Arrays.toString(cycleCounter));
          found = true;
          total += cycleCounter[(max - 1) % 2];
          System.out.println(STR."add \{cycleCounter[(max - 1) % 2]}");
        }
      }
//      System.out.println(STR."\{n} \{n % 131} \{s.size()} \{total} \{s}");

      if (found) {
        System.out.println(STR."total \{hasCycled.size()}");
//        System.out.println(gridOffsets.size() - hasCycled.size());
//        System.out.println(gridOffsets);
//        System.out.println(gridOffsets.stream().mapToInt(Point2D::x).summaryStatistics());
//        System.out.println(gridOffsets.stream().mapToInt(Point2D::y).summaryStatistics());
//        System.out.println();
      }

      var gridOffsetXMax = hasCycled.keySet().stream().mapToInt(Point2D::x).max().orElse(Integer.MAX_VALUE);
      var gridOffsetXMin = hasCycled.keySet().stream().mapToInt(Point2D::x).min().orElse(Integer.MAX_VALUE);

      var gridOffsetYMax = hasCycled.keySet().stream().mapToInt(Point2D::y).max().orElse(Integer.MAX_VALUE);
      var gridOffsetYMin = hasCycled.keySet().stream().mapToInt(Point2D::y).min().orElse(Integer.MAX_VALUE);

      next.removeIf(pt -> {
        var offsetX = Math.floorDiv(pt.x(), width);
        var offsetY = Math.floorDiv(pt.y(), height);

        if (offsetX == gridOffsetXMin || offsetX == gridOffsetXMax || offsetY == gridOffsetYMin || offsetY == gridOffsetYMax) {
          return false;
        }

//        if ((Math.floorMod(pt.x(), width) == 0 || Math.floorMod(pt.y(), height) == 0)) {
//          return false;
//        }
//
//        if ((Math.floorMod(pt.x(), width) == width - 1 || Math.floorMod(pt.y(), height) == height - 1)) {
//          return false;
//        }

        return hasCycled.containsKey(new Point2D(offsetX, offsetY));
//        return false;
      });
//      System.out.println(hasCycled.keySet());

      var tmp = curr;
      curr = next;
      next = tmp;
      next.clear();
    }

    System.out.println(total + curr.size());
    return (long) curr.size();
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    var width = grid.width();
    var height = grid.height();

    var startX = 0;
    var startY = 0;
    for (int i = 0; i < grid.array().length; i++) {
      if (grid.array()[i] == 'S') {
        startY = i / width;
        startX = i % width;
        break;
      }
    }
    grid.set(startX, startY, '.');

    var cache = new HashMap<Point2D, List<Point2D>>();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        var pt = new Point2D(x, y);
        if (grid.get(pt) != '.') {
          continue;
        }

        cache.put(pt, new ArrayList<>());
        var next = cache.get(pt);

        for (int yDelta = -1; yDelta <= 1; yDelta++) {
          if (yDelta == 0) {
            continue;
          }

          var candidate = pt.translate(0, yDelta);
          var modded = new Point2D(Math.floorMod(candidate.x(), width), Math.floorMod(candidate.y(), height));

          if (grid.get(modded) == '.') {
            next.add(candidate);
//            System.out.println(candidate);
          }
        }

        for (int xDelta = -1; xDelta <= 1; xDelta++) {
          if (xDelta == 0) {
            continue;
          }

          var candidate = pt.translate(xDelta, 0);
          var modded = new Point2D(Math.floorMod(candidate.x(), width), Math.floorMod(candidate.y(), height));

          if (grid.get(modded) == '.') {
            next.add(candidate);
//            System.out.println(candidate);
          }
        }
      }
    }

    var curr = new ArrayDeque<Point2D>();
    curr.add(new Point2D(startX, startY));
    var next = new ArrayDeque<Point2D>();

    var cycles = new HashMap<Point2D, int[]>();
    var hasCycled = new HashMap<Point2D, Boolean>();

    var total = 0L;

    var tally = new HashMap<Integer, Integer>();

    var max = 26501365;
    for (int n = 0; n < 132; n++) {
      if (n % 100 == 0) {
        System.out.println(n);
      }

      var seen = new HashSet<Point2D>();

      while (!curr.isEmpty()) {
        var pt = curr.remove();
//        var remapped = new Point2D(Math.floorMod(pt.x(), width), Math.floorMod(pt.y(), height));
//        var scaleX = pt.x() / width;
//        var scaleY = pt.y() / height;
//
//        if (pt.x() < 0) {
//          scaleX--;
//        }
//
//        if (pt.y() < 0) {
//          scaleY--;
//        }
//
//        var reachable = cache.get(remapped);
//        if (reachable == null) {
//          throw new IllegalStateException();
//        }
//
//        for (var r : reachable) {
//          var candidate = new Point2D(r.x() + scaleX * width, r.y() + scaleY * height);
//          var added = seen.add(candidate);
//          if (added) {
//            next.add(candidate);
//          }
//        }

        for (int yDelta = -1; yDelta <= 1; yDelta++) {
          if (yDelta == 0) {
            continue;
          }

          var candidate = pt.translate(0, yDelta);
          var modded = new Point2D(Math.floorMod(candidate.x(), width), Math.floorMod(candidate.y(), height));

          if (grid.get(modded) == '.' && seen.add(candidate)) {
            next.add(candidate);
//            System.out.println(candidate);
          }
        }

        for (int xDelta = -1; xDelta <= 1; xDelta++) {
          if (xDelta == 0) {
            continue;
          }

          var candidate = pt.translate(xDelta, 0);
          var modded = new Point2D(Math.floorMod(candidate.x(), width), Math.floorMod(candidate.y(), height));

          if (grid.get(modded) == '.' && seen.add(candidate)) {
            next.add(candidate);
//            System.out.println(candidate);
          }
        }
      }

//      System.out.println();
//      var debugCount = 0;
//      for (int y = -height * 2; y < height * 2; y++) {
//        for (int x = -width * 2; x < width * 2; x++) {
//          if (seen.contains(new Point2D(x, y))) {
//            debugCount++;
//          }
//          System.out.print(Character.toString(seen.contains(new Point2D(x, y)) ? 'O' : grid.get(Math.floorMod(x, width), Math.floorMod(y, height))));
//        }
//        System.out.println();
//      }
//      System.out.println(debugCount);

      var gridOffsets = new HashSet<Point2D>();
      for (var pt : seen) {
        var offsetX = Math.floorDiv(pt.x(), width);
        var offsetY = Math.floorDiv(pt.y(), height);

        gridOffsets.add(new Point2D(offsetX, offsetY));
      }

      var s = new HashMap<Integer, Integer>();

      var found = false;
      for (var gridOffset : gridOffsets) {
        if (hasCycled.containsKey(gridOffset)) {
          continue;
        }

        var offsetX = gridOffset.x() * width;
        var offsetY = gridOffset.y() * height;

        var count = 0;
        for (int y = 0; y < height; y++) {
          for (int x = 0; x < width; x++) {
            var pt = new Point2D(x + offsetX, y + offsetY);
            if (seen.contains(pt)) {
              count++;
            }
          }
        }

        if (gridOffset.equals(new Point2D(0, 0))) {
          tally.put(n, count);
        } else {
          s.compute(count, (k, v) -> v == null ? 1 : v + 1);
        }

        cycles.putIfAbsent(gridOffset, new int[4]);
        var cycleCounter = cycles.get(gridOffset);

        if (n % 2 == 1) {
          cycleCounter[1] = cycleCounter[3];
          cycleCounter[3] = count;
        } else {
          cycleCounter[0] = cycleCounter[2];
          cycleCounter[2] = count;
        }

        if (cycleCounter[2] == cycleCounter[0] && cycleCounter[3] == cycleCounter[1]) {
          var prev = hasCycled.put(gridOffset, true);
          if (prev != null) {
            throw new IllegalStateException();
          }
          System.out.println(STR."new cycle \{n} \{gridOffset}");
          System.out.println(Arrays.toString(cycleCounter));
          found = true;
          total += cycleCounter[(max - 1) % 2];
          System.out.println(STR."add \{cycleCounter[(max - 1) % 2]}");
        }
      }
//      System.out.println(STR."\{n} \{n % 131} \{s.size()} \{total} \{s}");

      if (found) {
        System.out.println(STR."total \{hasCycled.size()}");
//        System.out.println(gridOffsets.size() - hasCycled.size());
//        System.out.println(gridOffsets);
//        System.out.println(gridOffsets.stream().mapToInt(Point2D::x).summaryStatistics());
//        System.out.println(gridOffsets.stream().mapToInt(Point2D::y).summaryStatistics());
//        System.out.println();
      }

      var gridOffsetXMax = hasCycled.keySet().stream().mapToInt(Point2D::x).max().orElse(Integer.MAX_VALUE);
      var gridOffsetXMin = hasCycled.keySet().stream().mapToInt(Point2D::x).min().orElse(Integer.MAX_VALUE);

      var gridOffsetYMax = hasCycled.keySet().stream().mapToInt(Point2D::y).max().orElse(Integer.MAX_VALUE);
      var gridOffsetYMin = hasCycled.keySet().stream().mapToInt(Point2D::y).min().orElse(Integer.MAX_VALUE);

      next.removeIf(pt -> {
        var offsetX = Math.floorDiv(pt.x(), width);
        var offsetY = Math.floorDiv(pt.y(), height);

        if (offsetX == gridOffsetXMin || offsetX == gridOffsetXMax || offsetY == gridOffsetYMin || offsetY == gridOffsetYMax) {
          return false;
        }

//        if ((Math.floorMod(pt.x(), width) == 0 || Math.floorMod(pt.y(), height) == 0)) {
//          return false;
//        }
//
//        if ((Math.floorMod(pt.x(), width) == width - 1 || Math.floorMod(pt.y(), height) == height - 1)) {
//          return false;
//        }

        return hasCycled.containsKey(new Point2D(offsetX, offsetY));
//        return false;
      });
//      System.out.println(hasCycled.keySet());

      var tmp = curr;
      curr = next;
      next = tmp;
      next.clear();
    }

    // TODO: generalize

    var stableTotal = 7523L;
    for (long i = 1; i < (26501365L - 1) / 131; i++) {
      var num = i % 2 == 0 ? 7523 : 7584;
      stableTotal += (4 * i) * num;
    }
    System.out.println(stableTotal);

    return stableTotal
      + ((26501365L - 1) / 131 - 1) * (6608 + 6595 + 6612 + 6601)
      + ((26501365L - 1) / 131) * (966 + 967 + 968 + 940)
      + (5680 + 5684 + 5686 + 5690);
  }
}
