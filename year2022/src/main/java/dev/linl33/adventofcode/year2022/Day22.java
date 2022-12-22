package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.function.BiIntConsumer;
import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point3D;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;

public class Day22 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day22().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var parts = AdventUtil.readInputGrouped(reader).map(x -> x.toArray(String[]::new)).toArray(String[][]::new);
    var map = parts[0];
    var path = parts[1][0];

    var mapWidth = Arrays.stream(map).mapToInt(CharSequence::length).max().orElseThrow();
    var mapHeight = map.length;
    var grid = new RowArrayGrid(mapHeight, mapWidth);

    for (int y = 0; y < mapHeight; y++) {
      var s = map[y];
      for (int x = 0; x < s.length(); x++) {
        int c = s.charAt(x);
        if (c == ' ') {
          c = 0;
        }

        grid.set(x, y, c);
      }
    }

    var minX = new int[mapHeight];
    var maxX = new int[mapHeight];
    var minY = new int[mapWidth];
    var maxY = new int[mapWidth];

    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        var c = grid.get(x, y);
        if (c != 0) {
          minX[y] = x;
          break;
        }
      }

      for (int x = mapWidth - 1; x >= 0; x--) {
        var c = grid.get(x, y);
        if (c != 0) {
          maxX[y] = x;
          break;
        }
      }
    }

    for (int x = 0; x < mapWidth; x++) {
      for (int y = 0; y < mapHeight; y++) {
        var c = grid.get(x, y);
        if (c != 0) {
          minY[x] = y;
          break;
        }
      }

      for (int y = mapHeight - 1; y >= 0; y--) {
        var c = grid.get(x, y);
        if (c != 0) {
          maxY[x] = y;
          break;
        }
      }
    }

    var curr = new int[] { minX[0], 0, 0 };
    var wrap = new int[][] { minX, minY, maxX, maxY };

    walkPath(
        path,
        (magnitude, turn) -> {
          if (turn != 0) {
            curr[2] = turnHeading(curr[2], turn);
          }

          var delta = curr[2] < 2 ? 1 : -1;
          var axis = curr[2] % 2;

          for (int j = 0; j < magnitude; j++) {
            var backup = curr[axis];
            curr[axis] += delta;
            var next = gridGetSafe(grid, curr[0], curr[1]);

            if (next == '#') {
              curr[axis] = backup;
              break;
            } else if (next == 0) {
              curr[axis] = wrap[curr[2]][curr[1 - axis]];
              next = grid.get(curr[0], curr[1]);

              if (next == '#') {
                curr[axis] = backup;
                break;
              }
            }
          }
        }
    );

    return calculateFinalPassword(curr[0], curr[1], curr[2]);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    return part2Internal(reader, 1);
  }

  @VisibleForTesting
  public int part2Internal(@NotNull BufferedReader reader, int cubeNetId) {
    var parts = AdventUtil.readInputGrouped(reader).map(x -> x.toArray(String[]::new)).toArray(String[][]::new);
    var map = parts[0];
    var path = parts[1][0];

    var mapWidth = Arrays.stream(map).mapToInt(CharSequence::length).max().orElseThrow();
    var mapHeight = map.length;
    var grid = new RowArrayGrid(mapHeight, mapWidth);

    for (int y = 0; y < mapHeight; y++) {
      var s = map[y];
      for (int x = 0; x < s.length(); x++) {
        int c = s.charAt(x);
        if (c == ' ') {
          c = 0;
        }

        grid.set(x, y, c);
      }
    }

    var gridCount = 0;
    for (int i = 0; i < grid.array().length; i++) {
      if (grid.array()[i] != 0) {
        gridCount++;
      }
    }
    var cubeDim = (int) Math.sqrt(gridCount / 6);

    var minX = new int[mapHeight];
    var maxX = new int[mapHeight];
    var minY = new int[mapWidth];
    var maxY = new int[mapWidth];

    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        var c = grid.get(x, y);
        if (c != 0) {
          minX[y] = x;
          break;
        }
      }

      for (int x = mapWidth - 1; x >= 0; x--) {
        var c = grid.get(x, y);
        if (c != 0) {
          maxX[y] = x;
          break;
        }
      }
    }

    for (int x = 0; x < mapWidth; x++) {
      for (int y = 0; y < mapHeight; y++) {
        var c = grid.get(x, y);
        if (c != 0) {
          minY[x] = y;
          break;
        }
      }

      for (int y = mapHeight - 1; y >= 0; y--) {
        var c = grid.get(x, y);
        if (c != 0) {
          maxY[x] = y;
          break;
        }
      }
    }

    var faceGrid = new RowArrayGrid(mapHeight, mapWidth);
    var faceX = minX[0];
    var faceY = 0;
    for (int f = 1; f <= 6; f++) {
      for (int y = 0; y < cubeDim; y++) {
        for (int x = 0; x < cubeDim; x++) {
          faceGrid.set(faceX + x, faceY + y, f);
        }
      }

      if (f == 6) {
        continue;
      }

      faceX = faceX + cubeDim;
      if (faceX > maxX[faceY]) {
        faceY += cubeDim;
        faceX = minX[faceY];
      }
    }
    var faceMinX = new int[7];
    var faceMaxX = new int[7];
    var faceMinY = new int[7];
    var faceMaxY = new int[7];

    for (int i = 1; i < 7; i++) {
      var finalI = i;
      var x = IntStream.range(0, faceGrid.array().length).filter(p -> faceGrid.array()[p] == finalI).map(p -> p % mapWidth).min().orElseThrow();
      faceMaxX[i] = (faceMinX[i] = x) + cubeDim - 1;

      var y = IntStream.range(0, faceGrid.array().length).filter(p -> faceGrid.array()[p] == finalI).map(p -> p / mapWidth).min().orElseThrow();
      faceMaxY[i] = (faceMinY[i] = y) + cubeDim - 1;
    }

    var grid3d = new HashMap<Point3D, Integer>(cubeDim * 6);
    var translation = new HashMap<Point3D, Integer>(cubeDim * 6);

    for (int y = 0; y < mapHeight; y++) {
      for (int x = 0; x < mapWidth; x++) {
        var g = grid.get(x, y);
        if (g == 0) {
          continue;
        }

        var f = faceGrid.get(x, y);
        Point3D pt;
        // TODO: generalize
        if (cubeNetId == 0) {
          if (f == 1) {
          pt = new Point3D(x - faceMinX[f], y - faceMinY[f], -1);
          } else if (f == 2) {
          pt = new Point3D(faceMaxX[f] - x, -1, y - faceMinY[f]);
          } else if (f == 3) {
          pt = new Point3D(-1, x - faceMinX[f], y - faceMinY[f]);
          } else if (f == 4) {
          pt = new Point3D(x - faceMinX[f], cubeDim, y - faceMinY[f]);
          } else if (f == 5) {
          pt = new Point3D(x - faceMinX[f], faceMaxY[f] - y, cubeDim);
          } else if (f == 6) {
          pt = new Point3D(cubeDim, faceMaxY[f] - y, faceMaxX[f] - x);
          } else {
            throw new IllegalArgumentException();
          }
        } else if (cubeNetId == 1) {
          if (f == 1) {
            pt = new Point3D(x - faceMinX[f], y - faceMinY[f], -1);
          } else if (f == 2) {
            pt = new Point3D(cubeDim, y - faceMinY[f], x - faceMinX[f]);
          } else if (f == 3) {
            pt = new Point3D(x - faceMinX[f], cubeDim, y - faceMinY[f]);
          } else if (f == 4) {
            pt = new Point3D(-1, faceMaxY[f] - y, x - faceMinX[f]);
          } else if (f == 5) {
            pt = new Point3D(x - faceMinX[f], faceMaxY[f] - y, cubeDim);
          } else if (f == 6) {
            pt = new Point3D(y - faceMinY[f], -1, x - faceMinX[f]);
          } else {
            throw new IllegalArgumentException();
          }
        } else {
          throw new IllegalArgumentException();
        }

        var added = grid3d.put(pt, g);
        if (added != null) {
          throw new IllegalStateException();
        }

        translation.put(pt, y * mapWidth + x);
      }
    }

    var curr = new Point3D[] {
        new Point3D(0, 0, -1),
        new Point3D(1, 0, 0),
    };

    walkPath(
        path,
        (magnitude, turn) -> {
          if (turn != 0) {
            if (curr[0].x() == -1 || curr[0].y() == -1 || curr[0].z() == -1) {
              if (curr[0].x() == -1) {
                curr[1] = new Point3D(0, curr[1].z() * (turn == 'L' ? 1 : -1), curr[1].y() * (turn == 'L' ? -1 : 1));
              } else if (curr[0].y() == -1) {
                curr[1] = new Point3D(curr[1].z() * (turn == 'L' ? -1 : 1), 0, curr[1].x() * (turn == 'L' ? 1 : -1));
              } else {
                curr[1] = new Point3D(curr[1].y() * (turn == 'L' ? 1 : -1), curr[1].x() * (turn == 'L' ? -1 : 1), 0);
              }
            } else if (curr[0].x() == cubeDim || curr[0].y() == cubeDim || curr[0].z() == cubeDim) {
              if (curr[0].x() == cubeDim) {
                curr[1] = new Point3D(0, curr[1].z() * (turn == 'L' ? -1 : 1), curr[1].y() * (turn == 'L' ? 1 : -1));
              } else if (curr[0].y() == cubeDim) {
                curr[1] = new Point3D(curr[1].z() * (turn == 'L' ? 1 : -1), 0, curr[1].x() * (turn == 'L' ? -1 : 1));
              } else {
                curr[1] = new Point3D(curr[1].y() * (turn == 'L' ? -1 : 1), curr[1].x() * (turn == 'L' ? 1 : -1), 0);
              }
            } else {
              throw new IllegalStateException();
            }
          }

          var next = curr[0];
          for (int j = 0; j < magnitude; j++) {
            var backup = next;
            var vectorBackup = curr[1];

            next = next.translate(curr[1]);
            var nextVal = grid3d.get(next);

            if (nextVal == null) {
              if (curr[0].x() == -1 || curr[0].x() == cubeDim) {
                curr[1] = new Point3D(-Integer.signum(curr[0].x()), 0, 0);
              } else if (curr[0].y() == -1 || curr[0].y() == cubeDim) {
                curr[1] = new Point3D(0, -Integer.signum(curr[0].y()), 0);
              } else if (curr[0].z() == -1 || curr[0].z() == cubeDim) {
                curr[1] = new Point3D(0, 0, -Integer.signum(curr[0].z()));
              } else {
                throw new IllegalStateException();
              }

              next = next.translate(curr[1]);
              nextVal = grid3d.get(next);
              if (nextVal == null) {
                throw new IllegalStateException();
              } else if (nextVal == '#') {
                next = backup;
                curr[1] = vectorBackup;
                break;
              }
            } else if (nextVal == '#') {
              next = backup;
              break;
            }
          }

          curr[0] = next;
        }
    );

    var finalPos = translation.get(curr[0]);
    var finalX = finalPos % mapWidth;
    var finalY = finalPos / mapWidth;

    var front = curr[0].translate(curr[1]);
    var back = curr[0].translate(new Point3D(-curr[1].x(), -curr[1].y(), -curr[1].z()));
    var other = translation.containsKey(front) ? front : back;
    var otherPos = translation.get(other);
    var otherX = otherPos % mapWidth;
    var otherY = otherPos / mapWidth;

    int finalHeading;
    if (otherX == finalX) {
      finalHeading = 1 + (Integer.signum(finalY - otherY) + 1);
    } else {
      // noinspection PointlessArithmeticExpression
      finalHeading = 0 + (Integer.signum(finalX - otherX) + 1);
    }
    if (other == back) {
      finalHeading = (finalHeading + 2) % 4;
    }

    return calculateFinalPassword(finalX, finalY, finalHeading);
  }

  private static void walkPath(CharSequence path, BiIntConsumer onWalk) {
    var magnitude = 0;
    var turn = 0;
    for (int i = 0; i < path.length(); i++) {
      var p = path.charAt(i) - '0';

      if (p > 9) {
        turn = p + '0';
        continue;
      }

      for (; i < path.length(); i++) {
        p = path.charAt(i) - '0';
        if (p > 9) {
          i--;
          break;
        }

        magnitude = magnitude * 10 + p;
      }

      onWalk.accept(magnitude, turn);
      magnitude = 0;
      turn = 0;
    }
  }

  private static int gridGetSafe(Grid grid, int x, int y) {
    if (!grid.isWithinBounds(x, y)) {
      return 0;
    } else {
      return grid.get(x, y);
    }
  }

  private static int turnHeading(int heading, int turn) {
    // add 1 if R, add 3 if L
    return (heading + (85 - turn) / 3) % 4;
  }

  private static int calculateFinalPassword(int x, int y, int heading) {
    return 1000 * (y + 1) + 4 * (x + 1) + heading;
  }
}
