package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day6 extends AdventSolution2021<Long, Long> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    return solve(reader.readLine(), 80);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    return solve(reader.readLine(), 256);
  }

  private static long solve(@NotNull String initialState, int days) {
    var simulation = simulate(days);
    var sum = 0L;

    for (int i = 0; i < initialState.length(); i++) {
      var c = initialState.codePointAt(i);
      sum += c != ',' ? simulation[c - '0'] : 0;
    }

    return sum;
  }

  private static long[] simulate(int days) {
    // the last 2 days don't affect the result
    days -= 2;

    var generations = new int[9];
    generations[6] = 1;
    generations[8] = 1;

    var population = 2L;
    var finalPopulations = new long[9];

    for (int i = 6; i < days; i++) {
      var n = generations[i % 9];

      generations[(i + 7) % 9] += n;
      population += n;
      finalPopulations[(days - i) % 9] = (days - i) < 9 ? population : 0;
    }

    return finalPopulations;
  }
}
