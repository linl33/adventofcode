package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day21 extends AdventSolution2021<Integer, Long> {
  private static final int P1_WINNING_SCORE = 1000;
  private static final int P2_WINNING_SCORE = 21;
  private static final int[] MOVES_TO_U = new int[] { 0, 0, 0, 1, 3, 6, 7, 6, 3, 1 };

  public static void main(String[] args) {
    new Day21().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws IOException {
    var p1Line = reader.readLine();
    var p2Line = reader.readLine();

    var pos = new int[] {
        Integer.parseInt(p1Line, 28, p1Line.length(), 10),
        Integer.parseInt(p2Line, 28, p2Line.length(), 10),
    };

    var scores = new int[2];

    var moves = -3;
    var player = 0;
    while (scores[0] < P1_WINNING_SCORE && scores[1] < P1_WINNING_SCORE) {
      moves += 9;
      movePlayer(pos, scores, player, moves);

      player = 1 - player;
    }

    return scores[player] * ((moves + 3) / 9) * 3;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws IOException {
    var p1Line = reader.readLine();
    var p2Line = reader.readLine();

    var nullPos = new int[] {
        Integer.parseInt(p1Line, 28, p1Line.length(), 10),
        Integer.parseInt(p2Line, 28, p2Line.length(), 10),
    };

    var universes = new long[2];

    // TODO: make this run faster
    var stack = new ArrayDeque<List<Integer>>();
    for (int i = 9; i >= 3; i--) {
      stack.push(List.of(i));
    }

    while (!stack.isEmpty()) {
      var next = stack.pop();

      var pos = Arrays.copyOf(nullPos, 2);
      var scores = new int[2];
      var player = 0;

      for (int moves : next) {
        movePlayer(pos, scores, player, moves);
        player = 1 - player;
      }

      if (scores[0] >= P2_WINNING_SCORE || scores[1] >= P2_WINNING_SCORE) {
        var uTally = 1L;

        for (int moves : next) {
          uTally *= MOVES_TO_U[moves];
        }

        if (scores[0] >= P2_WINNING_SCORE) {
          universes[0] += uTally;
        } else {
          universes[1] += uTally;
        }
      } else {
        for (int i = 9; i >= 3; i--) {
          var nextMoves = new ArrayList<>(next);
          nextMoves.add(i);
          stack.push(nextMoves);
        }
      }
    }

    return Math.max(universes[0], universes[1]);
  }

  private static void movePlayer(@NotNull int[] pawnPositions, @NotNull int[] scores, int player, int moves) {
    var newPos = ((pawnPositions[player] + moves - 1) % 10) + 1;
    pawnPositions[player] = newPos;
    scores[player] += newPos;
  }
}
