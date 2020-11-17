package dev.linl33.adventofcode.year2019;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Day24 extends AdventSolution2019<Integer, Integer> {
  private static final int ERIS_SIZE = 25;
  private static final int ERIS_WIDTH = 5;
  private static final int[] ERIS_NEIGHBORS = new int[] {-1, 1, -ERIS_WIDTH, ERIS_WIDTH};

  public static void main(String[] args) {
    new Day24().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var eris = readEris(reader);
    var prevStates = new boolean[1 << ERIS_SIZE];

    while (!prevStates[eris]) {
      prevStates[eris] = true;
      eris = simulateEris(eris);
    }

    return eris;
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    return part2Internal(reader, 200);
  }

  public Integer part2Internal(BufferedReader reader, int minutes) {
    var eris = readEris(reader);

    var maxDepth = minutes + 2;
    var up = new int[maxDepth];
    var down = new int[maxDepth];

    up[0] = eris;
    down[0] = eris;

    for (int round = 0; round < minutes; round++) {
      var newUp = new int[maxDepth];
      var newDown = new int[maxDepth];

      for (int i = 1; i <= round + 1; i++) {
        newUp[i] = simulateErisRecursive(up[i], up[i + 1], up[i - 1]);
        newDown[i] = simulateErisRecursive(down[i], down[i - 1], down[i + 1]);
      }

      eris = simulateErisRecursive(eris, up[1], down[1]);
      newUp[0] = eris;
      newDown[0] = eris;

      up = newUp;
      down = newDown;
    }

    var bugCount = Integer.bitCount(eris);
    for (int i = 1; i < maxDepth; i++) {
      bugCount += Integer.bitCount(up[i]) + Integer.bitCount(down[i]);
    }

    return bugCount;
  }

  private static int readEris(BufferedReader reader) {
    var erisArr = reader
        .lines()
        .flatMap(line -> Arrays.stream(line.split("")))
        .mapToInt(Day24::erisStrToInt)
        .toArray();

    return IntStream
        .range(0, erisArr.length)
        .map(i -> erisArr[i] << i)
        .reduce(0, (left, right) -> left | right);
  }

  private static int erisStrToInt(String s) {
    return s.equals("#") ? 1 : 0;
  }

  private static int simulateEris(int state) {
    int newState = 0;

    for (int i = 0; i < ERIS_SIZE; i++) {
      var hasBug = (state & (1 << i)) > 0;

      var count = 0;
      for (int j : ERIS_NEIGHBORS) {
        var neighbor = i + j;
        if (neighbor < 0 || neighbor >= ERIS_SIZE) {
          continue;
        }

        if (j == -1 && i % ERIS_WIDTH == 0) {
          continue;
        }

        if (j == 1 && i % ERIS_WIDTH == (ERIS_WIDTH - 1)) {
          continue;
        }

        if ((state & (1 << neighbor)) > 0) {
          count++;
        }
      }

      if (hasBug) {
        if (count == 1) {
          newState |= 1 << i;
        }
      } else {
        if (count == 1 || count == 2) {
          newState |= 1 << i;
        }
      }
    }

    return newState;
  }

  private static int simulateErisRecursive(int state, int upState, int downState) {
    int newState = 0;

    for (int i = 0; i < ERIS_SIZE; i++) {
      if (i == 12) {
        continue;
      }

      var hasBug = (state & (1 << i)) > 0;

      var count = 0;
      for (int j : ERIS_NEIGHBORS) {
        var neighborIdx = i + j;

        // top edge
        if (j == -ERIS_WIDTH && neighborIdx < 0) {
          if ((upState & (1 << 7)) > 0) {
            count++;
          }

          continue;
        }

        // bottom edge
        if (j == ERIS_WIDTH && neighborIdx >= ERIS_SIZE) {
          if ((upState & (1 << 17)) > 0) {
            count++;
          }

          continue;
        }

        // left edge
        if (j == -1 && i % ERIS_WIDTH == 0) {
          if ((upState & (1 << 11)) > 0) {
            count++;
          }

          continue;
        }

        // right edge
        if (j == 1 && i % ERIS_WIDTH == (ERIS_WIDTH - 1)) {
          if ((upState & (1 << 13)) > 0) {
            count++;
          }

          continue;
        }

        if (neighborIdx == 12) {
          if (downState == 0) {
            continue;
          }

          if (i == 7) {
            for (int k = 0; k < ERIS_WIDTH; k++) {
              if ((downState & (1 << k)) > 0) {
                count++;
              }
            }
          } else if (i == 11) {
            for (int k = 0; k < ERIS_SIZE; k += ERIS_WIDTH) {
              if ((downState & (1 << k)) > 0) {
                count++;
              }
            }
          } else if (i == 13) {
            for (int k = 4; k < ERIS_SIZE; k += ERIS_WIDTH) {
              if ((downState & (1 << k)) > 0) {
                count++;
              }
            }
          } else if (i == 17) {
            for (int k = ERIS_SIZE - ERIS_WIDTH; k < ERIS_SIZE; k++) {
              if ((downState & (1 << k)) > 0) {
                count++;
              }
            }
          }

          continue;
        }

        if ((state & (1 << neighborIdx)) > 0) {
          count++;
        }
      }

      if (hasBug) {
        if (count == 1) {
          newState |= 1 << i;
        }
      } else {
        if (count == 1 || count == 2) {
          newState |= 1 << i;
        }
      }
    }

    return newState;
  }
}
