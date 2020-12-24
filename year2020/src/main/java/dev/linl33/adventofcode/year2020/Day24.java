package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day24 extends AdventSolution2020<Integer, Integer> {
  private enum Dir {
    E, W, NE, NW, SW, SE
  }

  private static final int GRID_WIDTH = 300;
  private static final int GRID_HEIGHT = GRID_WIDTH / 2;

  public static void main(String[] args) {
    new Day24().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    return (int) Arrays
        .stream(initFloor(reader).array())
        .filter(i -> i == ~0)
        .count();
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    var tiles = initFloor(reader);
    var copy = new RowArrayGrid(tiles.height(), tiles.width());

    var blackTileCount = 0;

    var minX = Integer.MAX_VALUE;
    var maxX = -1;
    var minY = Integer.MAX_VALUE;
    var maxY = -1;
    for (int y = 0; y < tiles.height(); y++) {
      for (int x = (y % 2 == 0 ? 1 : 0); x < tiles.width(); x += 2) {
        if (tiles.get(x, y) == 0) {
          continue;
        }

        blackTileCount++;

        minX = Math.min(minX, x);
        maxX = Math.max(maxX, x);
        minY = Math.min(minY, y);
        maxY = Math.max(maxY, y);
      }
    }

    for (int i = 0; i < 100; i++) {
      var newMinY = minY;
      var newMaxY = maxY;
      var newMinX = minX;
      var newMaxX = maxX;

      for (int y = minY - 1; y <= maxY + 1; y++) {
        // because of how the hexagonal grid is projected onto the orthogonal grid
        // if the parity of y == parity of x (i.e. y % 2 == x % 2), that position has to be equal to 0
        // adjust x here and iterate every other x
        for (int x = (y % 2 == (minX - 1) % 2) ? minX : minX - 1; x <= maxX + 1; x += 2) {
          int count = countNeighbors(tiles, y, x);

          if (tiles.get(x, y) == ~0) {
            if (count == 0 || count > 2) {
              copy.set(x, y, 0);
              blackTileCount--;
            } else {
              copy.set(x, y, ~0);
            }

            newMinY = Math.min(newMinY, y);
            newMinX = Math.min(newMinX, x);
            newMaxY = Math.max(newMaxY, y);
            newMaxX = Math.max(newMaxX, x);
          } else {
            if (count == 2) {
              copy.set(x, y, ~0);
              blackTileCount++;

              newMinY = Math.min(newMinY, y);
              newMinX = Math.min(newMinX, x);
              newMaxY = Math.max(newMaxY, y);
              newMaxX = Math.max(newMaxX, x);
            } else {
              copy.set(x, y, 0);
            }
          }
        }
      }

      minX = newMinX;
      minY = newMinY;
      maxX = newMaxX;
      maxY = newMaxY;

      var tmp = tiles;
      tiles = copy;
      copy = tmp;
    }

    return blackTileCount;
  }

  private static int countNeighbors(RowArrayGrid tiles, int y, int x) {
    var count = 0;

    if (tiles.get(x - 1, y + 1) == ~0) {
      count++;
    }

    if (tiles.get(x + 1, y - 1) == ~0) {
      count++;
    }

    if (tiles.get(x - 1, y - 1) == ~0) {
      count++;
    }

    if (tiles.get(x + 1, y + 1) == ~0) {
      count++;
    }

    if (tiles.get(x + 2, y) == ~0) {
      count++;
    }

    if (tiles.get(x - 2, y) == ~0) {
      count++;
    }
    return count;
  }

  private static RowArrayGrid initFloor(BufferedReader reader) {
    var floor = new RowArrayGrid(GRID_HEIGHT, GRID_WIDTH);

    reader
        .lines()
        .forEach(line -> {
          var posX = floor.width() / 2;
          var posY = floor.height() / 2;

          for (Dir d : parseDir(line)) {
            posX += switch (d) {
              case E -> 2;
              case W -> -2;
              case NE, SE -> 1;
              case NW, SW -> -1;
            };

            posY += switch (d) {
              case NE, NW -> 1;
              case SE, SW -> -1;
              default -> 0;
            };
          }

          floor.toggle(posX, posY);
        });

    return floor;
  }

  private static Dir[] parseDir(String line) {
    var charLine = line.toCharArray();
    var dirArr = new Dir[charLine.length];
    var size = 0;

    var carry = '\0';
    for (char c : charLine) {
      if (c == 'e') {
        Dir d;
        if (carry == '\0') {
          d = Dir.E;
        } else {
          d = carry == 's' ? Dir.SE : Dir.NE;
          carry = '\0';
        }
        dirArr[size++] = d;
      } else if (c == 'w') {
        Dir d;
        if (carry == '\0') {
          d = Dir.W;
        } else {
          d = carry == 's' ? Dir.SW : Dir.NW;
          carry = '\0';
        }
        dirArr[size++] = d;
      } else {
        carry = c;
      }
    }

    var output = new Dir[size];
    System.arraycopy(dirArr, 0, output, 0, size);
    return output;
  }
}
