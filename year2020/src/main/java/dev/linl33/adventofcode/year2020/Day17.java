package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;

import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Day17 extends AdventSolution2020<Integer, Integer> {
  private static final int SIM_ROUNDS = 6;

  public static void main(String[] args) {
    new Day17().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    return simulate(parseInitPlane(reader), 3);
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    // TODO:
//    return simulate(parseInitPlane(reader), 4);

    var grid = new RowArrayGrid(reader);

    var state = new TreeMap<Integer, Map<Integer, RowArrayGrid>>();
    state.computeIfAbsent(0, __ -> new TreeMap<>()).put(0, grid);

    var height = grid.height();
    var width = grid.width();

    for (int i = 0; i < 6; i++) {
      var newState = new TreeMap<Integer, Map<Integer, RowArrayGrid>>();

      var lowest = state.firstEntry().getKey();
      var highest = state.lastEntry().getKey();

      for (int metaLevel = lowest - 1; metaLevel <= highest + 1; metaLevel++) {
        var space = state.get(metaLevel);
        var newSpace = newState.computeIfAbsent(metaLevel + 1, __ -> new TreeMap<>());

        for (int level = lowest - 1; level <= highest + 1; level++) {
          var g = space == null ? null : space.get(level);

          for (int y = -1; y <= height; y++) {
            for (int x = -1; x <= width; x++) {
              int finalHeight = height;
              int finalWidth = width;
              newSpace
                  .computeIfAbsent(level + 1, __ -> {
                    var n = new RowArrayGrid(finalHeight + 2, finalWidth + 2);
                    Arrays.fill(n.array(), '.');

                    return n;
                  });

              newSpace.get(level + 1).set(x + 1, y + 1, gridAccessor(g, x, y));

              var nCount = sumNeighbors(state, x, y, level, metaLevel);

              if (gridAccessor(g, x, y) == '#' && (nCount != 3 && nCount != 4)) {
                newSpace.get(level + 1).set(x + 1, y + 1, '.');
              } else if (gridAccessor(g, x, y) == '.' && nCount == 3) {
                newSpace.get(level + 1).set(x + 1, y + 1, '#');
              }
            }
          }
        }
      }

      state = newState;
      width += 2;
      height += 2;
    }

    var sum = state
        .values()
        .stream()
        .map(Map::values)
        .flatMap(Collection::stream)
        .map(RowArrayGrid::array)
        .mapToInt(Array::getLength)
        .sum();

    System.out.println(sum);

    var corner = state
        .entrySet()
        .stream()
//        .filter(kv -> kv.getKey() > 0)
        .map(Map.Entry::getValue)
        .map(Map::entrySet)
        .flatMap(Collection::stream)
//        .filter(kv -> kv.getKey() > 0)
        .map(Map.Entry::getValue)
        .mapToInt(Day17::count)
        .sum();

//    var center = state.get(0).values().stream().mapToInt(Day17::count).sum();

    return corner;
  }

  private static int[] parseInitPlane(BufferedReader reader) {
    return reader
        .lines()
        .map(String::chars)
        .mapToInt(intStream -> intStream.reduce(
            0,
            (acc, cell) -> (acc << 1) | (cell == '#' ? 1 : 0)) << (SIM_ROUNDS + 1)
        )
        .toArray();
  }

  private static int simulate(int[] initPlane, int dim) {
    // TODO: avoid simulating the mirrored hyperspaces

    // TODO: WARNING!! initPlane needs to be left shifted

    // SIM_ROUNDS + 1 to avoid bounds check

    int planeDim = initPlane.length + 2 * (SIM_ROUNDS + 1);

    var planeCount = (int) Math.pow(1 + 2 * (SIM_ROUNDS + 1), dim - 2);
    var finalSize = planeDim * planeCount;

    var state = new int[finalSize];
    var stateNext = new int[finalSize];

    var currPlane = planeCount / 2;
    var dimensionalOffset = currPlane * planeDim;
    var planarOffset = SIM_ROUNDS + 1;
    var offset = dimensionalOffset + planarOffset;
    System.arraycopy(initPlane, 0, state, offset, initPlane.length);

    var width = initPlane.length;

    var dimSize = new int[dim - 1];
    dimSize[0] = planeDim;
    for (int i = 1; i < dimSize.length; i++) {
      dimSize[i] = dimSize[i - 1] * (1 + 2 * (SIM_ROUNDS + 1));
    }

    for (int round = 0; round < SIM_ROUNDS; round++) {
      var maskReset = 0b111 << (initPlane.length - 1 + SIM_ROUNDS + 1 + round);
      var cellMaskReset = 1 << (initPlane.length + SIM_ROUNDS + 1 + round);

      width += 2;

      // TODO: skip empty ones
      for (int row = /*dimensionalOffset - planeDim + (planarOffset - 1)*/ planeDim + 1; row < state.length - planeDim - 1; row++) {
        stateNext[row] = state[row];
        // TODO:

        // TODO: generalize for n-dim
/*        if (row % planeDim == initPlane.length + 2 * (SIM_ROUNDS + 1) - 1) {
          row++; // skip over the next row as well
          continue;
        }*/

        var neighborMask = maskReset;
        var cellMask = cellMaskReset;

        for (int col = 0; col < width; col++) {
          var neighbors = 0;
          for (int zDelta = -1; zDelta <= 1; zDelta++) {
            neighbors += Integer.bitCount(state[row + zDelta * planeDim - 1] & neighborMask) +
                Integer.bitCount(state[row + zDelta * planeDim] & neighborMask) +
                Integer.bitCount(state[row + zDelta * planeDim + 1] & neighborMask);
          }

          if (((state[row] & cellMask) != 0 && neighbors != 3 && neighbors != 4) ||
              ((state[row] & cellMask) == 0 && neighbors == 3)) {
            stateNext[row] ^= cellMask;
          }

          neighborMask >>= 1;
          cellMask >>= 1;
        }
      }

      var tmp = state;
      state = stateNext;
      stateNext = tmp;

//      System.out.println(Arrays.stream(state).map(Integer::bitCount).sum());
    }

    return Arrays.stream(state).map(Integer::bitCount).sum();
  }

  private static int sumNeighbors(Map<Integer, RowArrayGrid> levels, int x, int y, int z) {
    int count = 0;

    for (int zDelta = -1; zDelta <= 1; zDelta++) {
      var grid = levels == null ? null : levels.get(z + zDelta);

      for (int yDelta = -1; yDelta <= 1; yDelta++) {
        for (int xDelta = -1; xDelta <= 1; xDelta++) {
          if (gridAccessor(grid, x + xDelta, y + yDelta) == '#') {
            count++;
          }
        }
      }
    }

    return count;
  }

  private static int sumNeighbors(Map<Integer, Map<Integer, RowArrayGrid>> state, int x, int y, int z, int w) {
    int count = 0;

    for (int wDelta = -1; wDelta <= 1; wDelta++) {
      var space = state.get(w + wDelta);

      for (int zDelta = -1; zDelta <= 1; zDelta++) {
        var grid = space == null ? null : space.get(z + zDelta);

        for (int yDelta = -1; yDelta <= 1; yDelta++) {
          for (int xDelta = -1; xDelta <= 1; xDelta++) {
            if (gridAccessor(grid, x + xDelta, y + yDelta) == '#') {
              count++;
            }
          }
        }
      }
    }

    return count;
  }

  private static int gridAccessor(Grid g, int x, int y) {
    if (g == null) {
      return '.';
    }

    if (x < 0 || x >= g.width()) {
      return '.';
    }

    if (y < 0 || y >= g.height()) {
      return '.';
    }

    return g.get(x, y);
  }

  private static int count(RowArrayGrid grid) {
    var sum = 0;

    for (int y = 0; y < grid.height(); y++) {
      for (int x = 0; x < grid.width(); x++) {
        if (grid.get(x, y) == '#') {
          sum++;
        }
      }
    }

    return sum;
  }
}
