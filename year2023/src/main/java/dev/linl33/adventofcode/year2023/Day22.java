package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.point.Point3D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

public class Day22 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day22().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var supports = analyzeBrickSupport(reader);
    var singlePointsOfFailure = findSinglePointsOfFailure(supports);

    return supports.size() - singlePointsOfFailure.size();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var supports = analyzeBrickSupport(reader);
    var singlePointsOfFailure = findSinglePointsOfFailure(supports);

    // TODO: optimize traversal order, cache
    var total = 0;
    for (var pt : singlePointsOfFailure) {
      var removedBricks = new HashSet<Integer>();
      removedBricks.add(pt);

      boolean removed;
      do {
        removed = false;

        for (var kv : supports.entrySet()) {
          if (removedBricks.contains(kv.getKey()) || kv.getValue().isEmpty()) {
            continue;
          }

          if (removedBricks.containsAll(kv.getValue())) {
            removed = true;
            removedBricks.add(kv.getKey());
          }
        }
      } while (removed);

      total += (removedBricks.size() - 1);
    }

    return total;
  }

  private static Map<Integer, List<Integer>> analyzeBrickSupport(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var bricks = new Point3D[lines.length][];

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var ends = Arrays.stream(line.split("~"))
        .map(s -> s.split(","))
        .map(arr -> new Point3D(arr[0], arr[1], arr[2]))
        .toArray(Point3D[]::new);

      bricks[i] = ends;
    }
    Arrays.sort(bricks, Comparator.comparingInt(arr -> arr[0].z()));

    var grid = new HashMap<Point3D, Integer>();

    for (int i = 0; i < bricks.length; i++) {
      var brick = bricks[i];

      var bMin = brick[0];
      var bMax = brick[1];

      if (bMin.z() > 1) {
        var droppedZ = bMin.z();
        dropZLoop:
        for (; droppedZ > 0; droppedZ--) {
          for (int y = bMin.y(); y <= bMax.y(); y++) {
            for (int x = bMin.x(); x <= bMax.x(); x++) {
              var pt = new Point3D(x, y, droppedZ);
              if (grid.containsKey(pt)) {
                break dropZLoop;
              }
            }
          }
        }

        droppedZ++;
        var zDelta = droppedZ - bMin.z();
        bMin = bMin.translate(0, 0, zDelta);
        bMax = bMax.translate(0, 0, zDelta);
        bricks[i][0] = bMin;
        bricks[i][1] = bMax;
      }

      for (int y = bMin.y(); y <= bMax.y(); y++) {
        for (int x = bMin.x(); x <= bMax.x(); x++) {
          // only store the highest z, the other points are not necessary
          grid.put(new Point3D(x, y, bMax.z()), i);
        }
      }
    }

    var brickSupports = new HashMap<Integer, List<Integer>>();

    for (int i = 0; i < bricks.length; i++) {
      var brick = bricks[i];

      var bMin = brick[0];
      var bMax = brick[1];

      var supports = new HashSet<Integer>();
      var supportZ = bMin.z() - 1;

      for (int y = bMin.y(); y <= bMax.y(); y++) {
        for (int x = bMin.x(); x <= bMax.x(); x++) {
          var pt = new Point3D(x, y, supportZ);
          var val = grid.get(pt);
          if (val != null) {
            supports.add(val);
          }
        }
      }

      brickSupports.put(i, List.copyOf(supports));
    }

    return brickSupports;
  }

  private static Set<Integer> findSinglePointsOfFailure(Map<Integer, List<Integer>> supports) {
    return supports
      .values()
      .stream()
      .filter(arr -> arr.size() == 1)
      .flatMap(Collection::stream)
      .collect(Collectors.toSet());
  }
}
