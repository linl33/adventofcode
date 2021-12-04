package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.grid.ArrayGrid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Day4 extends AdventSolution2021<Integer, Integer> {
  private static final int BOARD_WIDTH = 5;
  private static final int BOARD_HEIGHT = 5;
  private static final Pattern SEQUENCE_SPLIT_PATTERN = Pattern.compile(",");

  public static void main(String[] args) {
    new Day4().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws IOException {
    var sequence = SEQUENCE_SPLIT_PATTERN.split(reader.readLine());
    // skip empty line
    reader.skip(1);

    var boards = new ArrayList<ArrayGrid>(100);
    while (reader.ready()) {
      boards.add(parseGrid(reader));
      reader.skip(1);
    }

    for (var seq : sequence) {
      var seqInt = Integer.parseInt(seq);

      for (var board : boards) {
        if (markBoard(board, seqInt)) {
          return scoreBoard(board) * seqInt;
        }
      }
    }

    throw new IllegalArgumentException();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws IOException {
    // solved by finding the number in each board that triggers the first bingo
    // then the boards are ranked by how late that number appears in the sequence

    var rank = new int[100];
    var list = new int[100];
    var listSize = 0;
    var digit = -1;
    var line = reader.readLine();

    for (int i = 0; i < line.length(); i++) {
      var c = line.charAt(i);
      if (c == ',') {
        list[listSize++] = digit;
        rank[digit] = listSize;
        digit = -1;
      } else {
        var cInt = c - '0';
        digit = digit < 0 ? cInt : 10 * digit + cInt;
      }
    }

    list[listSize] = digit;
    rank[digit] = listSize;

    // skip empty line
    reader.skip(1);

    var maxRank = 0;
    var board = new RowArrayGrid(BOARD_HEIGHT, BOARD_WIDTH);
    var maxBoard = board;

    while (reader.ready()) {
      var boardRank = Integer.MAX_VALUE;

      for (int row = 0; row < BOARD_HEIGHT; row++) {
        var next = reader.readLine();

        var rowRank = 0;

        for (int col = 0; col < BOARD_WIDTH; col++) {
          var left = next.charAt(col * 3);
          var right = next.charAt(col * 3 + 1);

          var value = (right - '0') + (left == ' ' ? 0 : (10 * (left - '0')));
          rowRank = Math.max(rowRank, rank[value]);

          board.set(col, row, value);
        }

        boardRank = Math.min(boardRank, rowRank);
      }

      if (boardRank < maxRank) {
        reader.skip(1);
        continue;
      }

      // test columns
      for (int col = 0; col < BOARD_WIDTH; col++) {
        var colRank = 0;

        for (int row = 0; row < BOARD_HEIGHT; row++) {
          var value = board.get(col, row);
          colRank = Math.max(colRank, rank[value]);

          if (colRank >= boardRank) {
            break;
          }
        }

        boardRank = Math.min(boardRank, colRank);

        if (boardRank < maxRank) {
          break;
        }
      }

      if (boardRank >= maxRank) {
        maxRank = boardRank;

        maxBoard = board;
        board = new RowArrayGrid(BOARD_HEIGHT, BOARD_WIDTH);
      }

      // skip empty line
      reader.skip(1);
    }

    var boardArray = maxBoard.array();
    var sum = 0;
    for (int i = 0; i < BOARD_WIDTH * BOARD_HEIGHT; i++) {
      var value = boardArray[i];
      var valueRank = rank[value];
      sum += valueRank <= maxRank && valueRank > 0 ? 0 : value;
    }

    return sum * list[maxRank - 1];
  }

  private static ArrayGrid parseGrid(@NotNull BufferedReader reader) throws IOException {
    var grid = new RowArrayGrid(BOARD_HEIGHT, BOARD_WIDTH);

    for (int row = 0; row < BOARD_HEIGHT; row++) {
      var line = reader.readLine();
      for (int col = 0; col < BOARD_WIDTH; col++) {
        var left = line.charAt(col * 3);
        var right = line.charAt(col * 3 + 1);

        var value = (right - '0') + (left == ' ' ? 0 : (10 * (left - '0')));
        grid.set(col, row, value);
      }
    }

    return grid;
  }

  private static boolean markBoard(@NotNull ArrayGrid board, int number) {
    var array = board.array();
    for (int i = 0; i < BOARD_WIDTH * BOARD_HEIGHT; i++) {
      if (array[i] == number) {
        var col = i % BOARD_WIDTH;
        var row = i / BOARD_HEIGHT;
        board.toggle(col, row);
        return isBingo(board, row, col);
      }
    }

    return false;
  }

  private static boolean isBingo(@NotNull ArrayGrid board, int row, int col) {
    int x;
    for (x = 0; x < BOARD_WIDTH; x++) {
      if (board.get(x, row) >= 0) {
        break;
      }
    }

    if (x == BOARD_WIDTH) {
      return true;
    }

    for (int y = 0; y < BOARD_HEIGHT; y++) {
      if (board.get(col, y) >= 0) {
        return false;
      }
    }

    return true;
  }

  private static int scoreBoard(@NotNull ArrayGrid board) {
    var array = board.array();
    var score = 0;

    for (int i = 0; i < BOARD_WIDTH * BOARD_HEIGHT; i++) {
      score += Math.max(array[i], 0);
    }

    return score;
  }
}
