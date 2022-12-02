package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.function.IntBinaryOperator;

public class Day2 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day2().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return scoreStrategyGuide(
        reader,
        (p1, p2) -> {
          // add 3 to make it positive
          var outcome = (p2 - p1 + 1 + 3) % 3;
          return scoreRound(outcome, p2);
        }
    );
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return scoreStrategyGuide(
        reader,
        (p1, outcome) -> {
          var offset = outcome + 2;
          var p2 = (p1 + offset) % 3;
          return scoreRound(outcome, p2);
        }
    );
  }

  private static int scoreStrategyGuide(@NotNull BufferedReader reader, @NotNull IntBinaryOperator op) {
    return reader
        .lines()
        .mapToInt(line -> op.applyAsInt(line.charAt(0) - 'A', line.charAt(2) - 'X'))
        .sum();
  }

  private static int scoreRound(int outcome, int p2) {
    return outcome * 3 + (p2 + 1);
  }
}
