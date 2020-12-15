package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Day15 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    new Day15().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws IOException {
    return solveForNthSingleHistory(reader, 2020);
  }

  @Override
  public Integer part2(BufferedReader reader) throws IOException {
    return solveForNthSingleHistory(reader, 30_000_000);
  }

  public static int solveForNth(BufferedReader reader, int n) throws IOException {
    var input = Arrays
        .stream(reader.readLine().split(","))
        .mapToInt(Integer::parseInt)
        .toArray();

    var history = new long[n];

    for (int i = 0; i < input.length; i++) {
      // put i + 1 into the lowest 32 bits
      history[input[i]] = i + 1L;
    }

    var number = input[input.length - 1];

    for (int i = input.length + 1; i <= n; i++) {
      var numberHistory = history[number];

      // if the highest 32 bits are not set then this number has only been seen once
      // if the number has been seen twice, subtract the highest 32 bits from the lowest 32 bits
      number = numberHistory < (1L << Integer.SIZE) ? 0 :
          (int) ((numberHistory & ~0) - (numberHistory >> Integer.SIZE));

      // shift the lowest 32 bits into the highest 32 bits
      // then put i into the lowest 32 bits
      history[number] = (history[number] << Integer.SIZE) | i;
    }

    return number;
  }

  private static int solveForNthSingleHistory(BufferedReader reader, int n) throws IOException {
    var input = Arrays
        .stream(reader.readLine().split(","))
        .mapToInt(Integer::parseInt)
        .toArray();

    // tracking 1 history is enough
    // the new number is always (n - 1) - (prior history)
    var history = new int[n];

    for (int i = 0; i < input.length; i++) {
      history[input[i]] = i + 1;
    }

    var number = input[input.length - 1];

    for (int i = input.length; i < n; i++) {
      var numberHistory = history[number];
      history[number] = i;

      number = numberHistory == 0 ? 0 : (i - numberHistory);
    }

    return number;
  }
}
