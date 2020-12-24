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
  private static final int BLACK_TILE = ~0;
  private static final int WHITE_TILE = 0;

  public static void main(String[] args) {
    new Day24().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    return (int) Arrays
        .stream(initFloor(reader).array())
        .filter(i -> i == BLACK_TILE)
        .count();
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    var tiles = initFloor(reader);
    var copy = new RowArrayGrid(GRID_HEIGHT, GRID_WIDTH);

    var tilesArr = tiles.array();
    var copyArr = copy.array();

    var blackTileCount = 0;

    var minX = Integer.MAX_VALUE;
    var maxX = -1;
    var minY = Integer.MAX_VALUE;
    var maxY = -1;
    for (int y = 0; y < GRID_HEIGHT; y++) {
      for (int x = (y % 2 == 0 ? 1 : 0); x < GRID_WIDTH; x += 2) {
        if (tiles.get(x, y) == WHITE_TILE) {
          continue;
        }

        blackTileCount++;

        if (x < minX) {
          minX = x;
        } else if (x > maxX) {
          maxX = x;
        }

        if (y < minY) {
          minY = y;
        } else if (y > maxY) {
          maxY = y;
        }
      }
    }

    var newMinY = minY;
    var newMaxY = maxY;
    var newMinX = minX;
    var newMaxX = maxX;

    for (int n = 0; n < 100; n++) {
      // bulk set copy to tiles
      // only arraycopy the region that matters
      System.arraycopy(
          tilesArr,
          (minY - 1) * GRID_WIDTH + (minX - 1),
          copyArr,
          (minY - 1) * GRID_WIDTH + (minX - 1),
          ((maxY + 1) * GRID_WIDTH + (maxX + 1)) - ((minY - 1) * GRID_WIDTH + (minX - 1)) + 1
      );

      for (int y = minY - 1; y <= maxY + 1; y++) {
        // because of how the hexagonal grid is projected onto the orthogonal grid
        // if the parity of y == parity of x (i.e. y % 2 == x % 2), that position has to be equal to 0
        // adjust x here and iterate every other x

        var setY = false;
        for (int x = (y % 2 == (minX - 1) % 2) ? minX : minX - 1; x <= maxX + 1; x += 2) {
          var count = countNeighbors(tiles, y, x);

          if (tiles.get(x, y) == BLACK_TILE) {
            if (count == 0 || count > 2) {
              copy.set(x, y, WHITE_TILE);
              blackTileCount--;
            }

            setY = true;
            if (x < minX) {
              newMinX = x;
            } else if (x > maxX) {
              newMaxX = x;
            }
          } else {
            if (count == 2) {
              copy.set(x, y, BLACK_TILE);
              blackTileCount++;

              setY = true;
              if (x < minX) {
                newMinX = x;
              } else if (x > maxX) {
                newMaxX = x;
              }
            }
          }
        }

        if (setY) {
          if (y < minY) {
            newMinY = y;
          } else if (y > maxY) {
            newMaxY = y;
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

      var tmpArr = tilesArr;
      tilesArr = copyArr;
      copyArr = tmpArr;
    }

    return blackTileCount;
  }

  private static int countNeighbors(RowArrayGrid tiles, int y, int x) {
    return (tiles.get(x - 1, y - 1) & 1) +
        (tiles.get(x - 1, y + 1) & 1) +
        (tiles.get(x + 1, y - 1) & 1) +
        (tiles.get(x + 1, y + 1) & 1) +
        (tiles.get(x - 2, y) & 1) +
        (tiles.get(x + 2, y) & 1);
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
