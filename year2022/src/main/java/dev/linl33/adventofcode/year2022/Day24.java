package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class Day24 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day24().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
//    var lines = reader.lines().toList();
    var grid = new RowArrayGrid(reader);

    var start = new Point2D(1, 0);
    var endPos = grid.array().length - 2;
    var end = new Point2D(grid.array()[endPos], 0);
//    System.out.println((char) grid.array()[grid.array().length - 1]);
//    System.out.println((char) grid.array()[grid.array().length - 2]);

    var gridArray = grid.array();
    for (int i = 0; i < gridArray.length; i++) {
      var c = gridArray[i];

      if (c == '.') {
        gridArray[i] = 0;
      } else if (c == '>' || c == '<' || c == '^' || c == 'v') {
        gridArray[i] = switch (c) {
          case '>' -> 0b0001;
          case 'v' -> 0b0010;
          case '<' -> 0b0100;
          case '^' -> 0b1000;
          default -> throw new IllegalArgumentException();
        };
      } else if (c == '#') {
        gridArray[i] = -1;
      }
    }

    var blizzardForecast = new int[10_000][];

    var path = GraphUtil.aStar(
        start,
        end,
        positionTime -> {
          var pos = positionTime.x();
          var time = positionTime.y();

          var blizzardCondition = blizzardForecast[time + 1];
          if (blizzardCondition == null) {
            blizzardForecast[time + 1] = simulateBlizzard(gridArray, grid.width(), time + 1);
            blizzardCondition = blizzardForecast[time + 1];
          }

          var neighbors = new ArrayList<Point2D>(5);
          if (blizzardCondition[pos] == 0) {
            neighbors.add(positionTime.translate(0, 1));
          }

          for (int deltaY = -1; deltaY <= 1; deltaY++) {
            for (int deltaX = -1; deltaX <= 1; deltaX++) {
              if ((deltaX == 0) == (deltaY == 0)) {
                continue;
              }

              var posX = pos % grid.width();
              var posY = pos / grid.width();
              var posNext = (posY + deltaY) * grid.width() + (posX + deltaX);

              if (!grid.isWithinBounds(posX + deltaX, posY + deltaY)) {
                continue;
              }

//              if (posY + deltaY < 0) {
//                continue;
//              }

              if (posNext == endPos) {
                return List.of(end);
              }

              if (blizzardCondition[posNext] == 0) {
                neighbors.add(new Point2D(posNext, time + 1));
              }
            }
          }

//          var curr = positionTime;
//          System.out.println("time = " + curr.y() + ", x = " + (curr.x() % grid.width()) + ", y = " + (curr.x() / grid.width()) + ", neighbors = " + neighbors);
          return neighbors;
        }
    );

//    var b = blizzardForecast[18];
//    for (int y = 0; y < grid.height(); y++) {
//      var sb = new StringBuilder();
//      for (int x = 0; x < grid.width(); x++) {
//        var p = y * grid.width() + x;
//        var c = b[p];
//
//        if (c == 0) {
//          sb.append('.');
//        } else if (c == -1) {
//          sb.append('#');
//        } else if (c == 0b0001) {
//          sb.append('>');
//        } else if (c == 0b0010) {
//          sb.append('v');
//        } else if (c == 0b0100) {
//          sb.append('<');
//        } else if (c == 0b1000) {
//          sb.append('^');
//        } else {
//          sb.append(Integer.bitCount(c));
//        }
//      }
//
//      System.out.println(sb);
//    }

    var p = path.orElseThrow();
//    System.out.println(p);
//    System.out.println(p.length() + 1);

//    var curr = start;
//    while(curr != end) {
//      System.out.println("time = " + curr.y() + ", x = " + (curr.x() % grid.width()) + ", y = " + (curr.x() / grid.width()));
//      curr = p.path().get(curr);
//    }
//    System.out.println("time = " + curr.y() + ", x = " + (curr.x() % grid.width()) + ", y = " + (curr.x() / grid.width()));

    return p.length();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    //    var lines = reader.lines().toList();
    var grid = new RowArrayGrid(reader);

    var entrancePos = 1;
    var start = new Point2D(entrancePos, 0);
    var exitPos = grid.array().length - 2;
    var end = new Point2D(exitPos, 0);
//    var end2 = new Point2D(1, 0);
//    System.out.println((char) grid.array()[grid.array().length - 1]);
//    System.out.println((char) grid.array()[grid.array().length - 2]);

    var gridArray = grid.array();
    for (int i = 0; i < gridArray.length; i++) {
      var c = gridArray[i];

      if (c == '.') {
        gridArray[i] = 0;
      } else if (c == '>' || c == '<' || c == '^' || c == 'v') {
        gridArray[i] = switch (c) {
          case '>' -> 0b0001;
          case 'v' -> 0b0010;
          case '<' -> 0b0100;
          case '^' -> 0b1000;
          default -> throw new IllegalArgumentException();
        };
      } else if (c == '#') {
        gridArray[i] = -1;
      }
    }

    var blizzardForecast = new int[10_000][];

    Function<Point2D, Collection<Point2D>> neighborFunc = positionTime -> {
      var pos = positionTime.x();
      var time = positionTime.y();

      var blizzardCondition = blizzardForecast[time + 1];
      if (blizzardCondition == null) {
        blizzardForecast[time + 1] = simulateBlizzard(gridArray, grid.width(), time + 1);
        blizzardCondition = blizzardForecast[time + 1];
      }

      var neighbors = new ArrayList<Point2D>(5);
      if (blizzardCondition[pos] == 0) {
        neighbors.add(positionTime.translate(0, 1));
      }

      for (int deltaY = -1; deltaY <= 1; deltaY++) {
        for (int deltaX = -1; deltaX <= 1; deltaX++) {
          if ((deltaX == 0) == (deltaY == 0)) {
            continue;
          }

          var posX = pos % grid.width();
          var posY = pos / grid.width();
          var posNext = (posY + deltaY) * grid.width() + (posX + deltaX);

          if (!grid.isWithinBounds(posX + deltaX, posY + deltaY)) {
            continue;
          }

          if (posNext == exitPos) {
            return List.of(end);
          }

          if (blizzardCondition[posNext] == 0) {
            neighbors.add(new Point2D(posNext, time + 1));
          }
        }
      }

//          var curr = positionTime;
//          System.out.println("time = " + curr.y() + ", x = " + (curr.x() % grid.width()) + ", y = " + (curr.x() / grid.width()) + ", neighbors = " + neighbors);
      return neighbors;
    };

    Function<Point2D, Collection<Point2D>> neighborFunc2 = positionTime -> {
      var pos = positionTime.x();
      var time = positionTime.y();

//      assert blizzardForecast[time][pos] == 0;

      var blizzardCondition = blizzardForecast[time + 1];
      if (blizzardCondition == null) {
        blizzardForecast[time + 1] = simulateBlizzard(gridArray, grid.width(), time + 1);
        blizzardCondition = blizzardForecast[time + 1];
      }

      var neighbors = new ArrayList<Point2D>(5);
      if (blizzardCondition[pos] == 0) {
        neighbors.add(positionTime.translate(0, 1));
      }

      for (int deltaY = -1; deltaY <= 1; deltaY++) {
        for (int deltaX = -1; deltaX <= 1; deltaX++) {
          if ((deltaX == 0) == (deltaY == 0)) {
            continue;
          }

          var posX = pos % grid.width();
          var posY = pos / grid.width();
          var posNext = (posY + deltaY) * grid.width() + (posX + deltaX);

          if (!grid.isWithinBounds(posX + deltaX, posY + deltaY)) {
            continue;
          }

          if (posNext == entrancePos) {
            assert blizzardCondition[entrancePos] == 0;
            return List.of(start);
          }

          if (blizzardCondition[posNext] == 0) {
            neighbors.add(new Point2D(posNext, time + 1));
          }
        }
      }

//          var curr = positionTime;
//          System.out.println("time = " + curr.y() + ", x = " + (curr.x() % grid.width()) + ", y = " + (curr.x() / grid.width()) + ", neighbors = " + neighbors);
      return neighbors;
    };

    var path = GraphUtil.aStar(
        start,
        end,
        neighborFunc
    );

//    var b = blizzardForecast[18];
//    for (int y = 0; y < grid.height(); y++) {
//      var sb = new StringBuilder();
//      for (int x = 0; x < grid.width(); x++) {
//        var p = y * grid.width() + x;
//        var c = b[p];
//
//        if (c == 0) {
//          sb.append('.');
//        } else if (c == -1) {
//          sb.append('#');
//        } else if (c == 0b0001) {
//          sb.append('>');
//        } else if (c == 0b0010) {
//          sb.append('v');
//        } else if (c == 0b0100) {
//          sb.append('<');
//        } else if (c == 0b1000) {
//          sb.append('^');
//        } else {
//          sb.append(Integer.bitCount(c));
//        }
//      }
//
//      System.out.println(sb);
//    }

    var p = path.orElseThrow();
    var totalLength = p.length();
    System.out.println("S1 = " + p.length());

//    System.out.println(p);
//    System.out.println(p.length() + 1);

//    var curr = start;
//    while(curr != end) {
//      System.out.println("time = " + curr.y() + ", x = " + (curr.x() % grid.width()) + ", y = " + (curr.x() / grid.width()));
//      curr = p.path().get(curr);
//    }
//    System.out.println("time = " + curr.y() + ", x = " + (curr.x() % grid.width()) + ", y = " + (curr.x() / grid.width()));

    var start2 = new Point2D(exitPos, totalLength);
    System.out.println("start2 = " + start2);

    path = GraphUtil.aStar(
        start2,
        start,
        neighborFunc2
    );

    p = path.orElseThrow();
    totalLength += p.length();
    System.out.println("S2 = " + p.length());

    var start3 = new Point2D(1, totalLength);
    System.out.println("start3  = " + start3);

    path = GraphUtil.aStar(
        start3,
        end,
        neighborFunc
    );

    p = path.orElseThrow();
    totalLength += p.length();
    System.out.println("S3 = " + p.length());

    System.out.println(path);

    return totalLength;
  }

  private static int[] simulateBlizzard(int[] initialState, int width, int steps) {
    var copy = Arrays.copyOf(initialState, initialState.length);
    var next = new int[initialState.length];

    for (int step = 0; step < steps; step++) {
      Arrays.fill(next, 0);

      for (int i = 0; i < initialState.length; i++) {
        var c = copy[i];

        if (c == 0) {
          continue;
        }

        if (c == -1) {
          next[i] = -1;
          continue;
        }

        var cBackup = c;

        for (int j = 0, mask = 1; j < 4; j++, mask <<= 1) {
          c = cBackup & mask;
          if (c == 0) {
            continue;
          }

          var nextIdx = switch (c) {
            case 0b0001 -> i + 1;
            case 0b0010 -> i + width;
            case 0b0100 -> i - 1;
            case 0b1000 -> i - width;
            default -> throw new IllegalArgumentException();
          };

          if (copy[nextIdx] == -1) {
            nextIdx = switch (c) {
//              case 0b0001 -> i - (width - 3);
              case 0b0001 -> (i / width) * width + 1;
              case 0b0010 -> width + (i % width);
//              case 0b0100 -> i + (width - 3);
              case 0b0100 -> (i / width + 1) * width - 2;
//              case 0b1000 -> initialState.length - 1 - width - (width - (i % width) - 1);
              case 0b1000 -> (initialState.length / width - 2) * width + (i % width);
              default -> throw new IllegalArgumentException();
            };
          }

          next[nextIdx] |= c;
        }
      }

      var tmp = copy;
      copy = next;
      next = tmp;
    }

    return copy;
  }
}
