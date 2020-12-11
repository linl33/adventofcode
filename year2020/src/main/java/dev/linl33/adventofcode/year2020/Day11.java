package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.grid.RowArrayGrid;

import java.io.BufferedReader;
import java.util.function.IntPredicate;

public class Day11 extends AdventSolution2020<Integer, Integer> {
  private static final int FLOOR = '.';
  private static final int EMPTY_SEAT = 'L';
  private static final int OCCUPIED_SEAT = '#';
  private static final int MAX_OCCUPATION_THRESHOLD = 5;

  public static void main(String[] args) {
    new Day11().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return simulate(
        reader,
        (grid, x, y, deltaX, deltaY) ->
            grid.isWithinBounds(x + deltaX, y + deltaY) &&
                grid.get(x + deltaX, y + deltaY) == OCCUPIED_SEAT,
        occupiedCount -> occupiedCount == 0,
        occupiedCount -> occupiedCount >= 4
    );
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return simulate(
        reader,
        (grid, x, y, deltaX, deltaY) -> {
          var neighbor = 0;

          do {
            y += deltaY;
            x += deltaX;
          } while (grid.isWithinBounds(x, y) &&
              (neighbor = grid.get(x, y)) == FLOOR);

          return neighbor == OCCUPIED_SEAT;
        },
        occupiedCount -> occupiedCount == 0,
        occupiedCount -> occupiedCount >= 5
    );
  }

  private static int simulate(BufferedReader reader,
                              GridOffsetPredicate gridOffsetPredicate,
                              IntPredicate emptySeatTransition,
                              IntPredicate occupiedSeatTransition) {
    var grid = new RowArrayGrid(reader);
    var height = grid.height();
    var width = grid.width();

    int moveCount;
    var occupiedSeats = 0;
    var gridNext = new RowArrayGrid(height, width);

    do {
      moveCount = 0;

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          var seat = grid.get(x, y);
          gridNext.set(x, y, seat);

          if (seat == FLOOR) {
            continue;
          }

          var occupiedCount = 0;

          for (int deltaY = -1; deltaY <= 1; deltaY++) {
            for (int deltaX = -1; deltaX <= 1; deltaX++) {
              if (deltaY == 0 && deltaX == 0) {
                continue;
              }

              if (occupiedCount < MAX_OCCUPATION_THRESHOLD &&
                  gridOffsetPredicate.isOccupied(grid, x, y, deltaX, deltaY)) {
                occupiedCount++;
              }
            }
          }

          var rule = switch (seat) {
            case EMPTY_SEAT -> emptySeatTransition;
            case OCCUPIED_SEAT -> occupiedSeatTransition;
            default -> throw new IllegalStateException();
          };

          if (rule.test(occupiedCount)) {
            gridNext.set(x, y, seat ^ EMPTY_SEAT ^ OCCUPIED_SEAT);

            moveCount++;
            occupiedSeats += seat == EMPTY_SEAT ? 1 : -1;
          }
        }
      }

      var tmp = grid;
      grid = gridNext;
      gridNext = tmp;
    } while (moveCount > 0);

    // assumes that every occupied seat transitions at least once
    return occupiedSeats;
  }

  @FunctionalInterface
  interface GridOffsetPredicate {
    boolean isOccupied(RowArrayGrid grid, int x, int y, int deltaX, int deltaY);
  }
}
