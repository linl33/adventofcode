package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Day17 extends AdventSolution2022<Long, Long> {
  private static final int PART_1_ROCKS = 2022;
  private static final long PART_2_ROCKS = 1000000000000L;
  private static final int WIDTH = 7;

  private static final int[][] Y_OFFSET_BOTTOM = {
      new int[] { 0, 0, 0, 0 },
      new int[] { 1, 0, 1 },
      new int[] { 0, 0, 0 },
      new int[] { 0 },
      new int[] { 0, 0 },
  };
  private static final int[][] Y_OFFSET_TOP = {
      new int[] { 0, 0, 0, 0 },
      new int[] { 1, 2, 1 },
      new int[] { 0, 0, 2 },
      new int[] { 3 },
      new int[] { 1, 1 },
  };
  private static final int[] Y_OFFSET_MAX = { 0, 2, 2, 3, 1 };
  private static final int[] SHAPE_WIDTH = { 4, 3, 3, 1, 2 };

  public static void main(String[] args) {
    // TODO: clean up
    new Day17().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);
    var jetPattern = lines[0];

    var grid = new HashSet<Point2D>();
    var maxY = -1;

    for (int x = 0; x < WIDTH; x++) {
      grid.add(new Point2D(x, maxY));
    }

    var jetPointer = 0;

    for (int i = 0; i < PART_1_ROCKS; i++) {
      var shape = i % 5;

      var xMinShape = 2;
      for (int j = 0; j < 3; j++) {
        xMinShape = translateX(xMinShape, readJet(jetPattern, jetPointer), shape);
        jetPointer++;
      }

      var yMinShape = maxY + 1;

      while (true) {
        var delta = readJet(jetPattern, jetPointer);
        var xNext = translateX(xMinShape, delta, shape);
        jetPointer++;
        if (xNext != xMinShape) {
          var shapeNext = makeShape(shape, xNext, yMinShape);
          var xNextValid = isShapeValid(grid, shapeNext);
          if (xNextValid) {
            xMinShape = xNext;
          }
        }

        var yNext = yMinShape - 1;
        var shapeNext = makeShape(shape, xMinShape, yNext);
        var yNextValid = isShapeValid(grid, shapeNext);
        if (yNextValid) {
          yMinShape = yNext;
        } else {
          break;
        }
      }

      var shapeToCommit = makeShape(shape, xMinShape, yMinShape);
      grid.addAll(shapeToCommit);

      maxY = Math.max(maxY, yMinShape + Y_OFFSET_MAX[shape]);
    }

    return (long) maxY + 1;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);
    var jetPattern = lines[0];

    var grid = new HashSet<Point2D>();
    var gridHistory = new HashMap<HashSet<Point2D>, Integer>();
    var gridHeightHistory = new HashMap<Integer, Integer>();
    var gridTupleHistory = new HashSet<String>();

    var bottomRow = -1;
    var maxY = bottomRow;

    var absoluteHeight = bottomRow;

    for (int x = 0; x < WIDTH; x++) {
      grid.add(new Point2D(x, maxY));
    }

    var jetPointer = 0;
    var simulationLimit = 10_000;

    var cycleFound = false;
    var h1 = 0;
    var h2 = 0L;

    rockLoop:
    for (int i = 0; i < simulationLimit; i++) {
      var shape = i % 5;

      var xMinShape = 2;
      for (int j = 0; j < 3; j++) {
        xMinShape = translateX(xMinShape, readJet(jetPattern, jetPointer), shape);
        jetPointer++;
      }

      var yMinShape = maxY + 1;

      while (true) {
        var delta = readJet(jetPattern, jetPointer);
        var xNext = translateX(xMinShape, delta, shape);
        jetPointer++;
        if (xNext != xMinShape) {
          var shapeNext = makeShape(shape, xNext, yMinShape);
          var xNextValid = isShapeValid(grid, shapeNext);
          if (xNextValid) {
            xMinShape = xNext;
          }
        }

        var yNext = yMinShape - 1;
        var shapeNext = makeShape(shape, xMinShape, yNext);
        var yNextValid = isShapeValid(grid, shapeNext);
        if (yNextValid) {
          yMinShape = yNext;
        } else {
          break;
        }
      }

      var shapeToCommit = makeShape(shape, xMinShape, yMinShape);
      grid.addAll(shapeToCommit);

      for (int row = yMinShape + Y_OFFSET_MAX[shape]; row >= yMinShape; row--) {
        var isFilled = isRowFilled(grid, row);
        if (isFilled) {
          var bottomDelta = row - bottomRow;
          maxY -= bottomDelta;
          yMinShape -= bottomDelta;
          maxY = Math.max(maxY, yMinShape + Y_OFFSET_MAX[shape]);

          absoluteHeight += bottomDelta;

          var gridNew = new HashSet<Point2D>();
          for (var point2D : grid) {
            if (point2D.y() >= row) {
              gridNew.add(point2D.translate(0, -bottomDelta));
            }
          }

          grid = gridNew;
          if (!cycleFound) {
            gridHistory.putIfAbsent(new HashSet<>(grid), i);
            var gridIdx = gridHistory.get(grid);
            var gridTuple = gridIdx + " " + shape + " " + (jetPointer % jetPattern.length());
            var added = gridTupleHistory.add(gridTuple);

            gridHeightHistory.putIfAbsent(gridIdx, maxY - bottomRow + 1 + absoluteHeight);

            if (!added) {
              cycleFound = true;

              var m = i - gridIdx;
              var b = gridIdx;
              var remainder = (PART_2_ROCKS - b) % m;

              var quotient = PART_2_ROCKS - remainder;

              var currHeight = maxY - bottomRow + 1 + absoluteHeight;
              var prevHeight = gridHeightHistory.get(gridIdx);
              var quotientHeight = (currHeight - prevHeight) * (quotient - gridIdx) / m + prevHeight;

              h1 = currHeight;
              h2 = quotientHeight;

              simulationLimit = (int) (i + remainder);
              continue rockLoop;
            }
          }

          break;
        }
      }

      maxY = Math.max(maxY, yMinShape + Y_OFFSET_MAX[shape]);
    }

    var h3 = maxY - bottomRow + 1 + absoluteHeight;
    return h2 + (h3 - h1);
  }

  private static Set<Point2D> makeShape(int shapeId, int x, int y) {
    var set = new HashSet<Point2D>(5);
    for (int i = 0; i < SHAPE_WIDTH[shapeId]; i++) {
      set.add(new Point2D(x + i, y + Y_OFFSET_BOTTOM[shapeId][i]));
      set.add(new Point2D(x + i, y + Y_OFFSET_TOP[shapeId][i]));
    }

    if (shapeId == 1) {
      set.add(new Point2D(x + 1, y + 1));
    } else if (shapeId == 2) {
      set.add(new Point2D(x + 2, y + 1));
    } else if (shapeId == 3) {
      set.add(new Point2D(x, y + 1));
      set.add(new Point2D(x, y + 2));
    }

    return set;
  }

  private static int readJet(String jetPattern, int start) {
    return jetPattern.charAt(start % jetPattern.length()) == '>' ? 1 : -1;
  }

  private static int translateX(int x, int delta, int shapeId) {
    return Math.max(0, Math.min(shapeMaxX(shapeId), x + delta));
  }

  private static int shapeMaxX(int shapeId) {
    return WIDTH - 1 - SHAPE_WIDTH[shapeId] + 1;
  }

  private static void printGrid(Set<Point2D> grid, int maxY) {
    var arrGrid = new RowArrayGrid(maxY + 1, WIDTH);
    Arrays.fill(arrGrid.array(), '.');
    for (Point2D point2D : grid) {
      if (point2D.y() < 0) {
        continue;
      }
      arrGrid.set(point2D, '#');
    }
    arrGrid.invertY();
    arrGrid.print();
    System.out.println();
  }

  private static boolean isShapeValid(Set<Point2D> grid, Set<Point2D> shape) {
    for (var s : shape) {
      if (grid.contains(s)) {
        return false;
      }
    }

    return true;
  }

  private static boolean isRowFilled(Set<Point2D> grid, int row) {
    for (int i = 0; i < WIDTH; i++) {
      if (!grid.contains(new Point2D(i, row))) {
        return false;
      }
    }

    return true;
  }
}
