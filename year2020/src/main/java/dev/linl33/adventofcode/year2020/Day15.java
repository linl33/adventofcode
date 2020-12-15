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
    return solveForNth(reader, 2020);
  }

  @Override
  public Integer part2(BufferedReader reader) throws IOException {
    return solveForNth(reader, 30_000_000);
  }

  private static int solveForNth(BufferedReader reader, int n) throws IOException {
    var input = Arrays
        .stream(reader.readLine().split(","))
        .mapToInt(Integer::parseInt)
        .toArray();

    var history = new long[n];

    for (int i = 0; i < input.length; i++) {
      // put i into the lowest 32 bits
      history[input[i]] = i + 1L;
    }

    var number = input[input.length - 1];

    for (int i = input.length + 1; i <= n; i++) {
      var numberHistory = history[number];

      // if the highest 32 bits are not set then this number has only been seen once
      // if the number has been seen twice, subtract the highest 32 bits from the lowest 32 bits
      number = (numberHistory >> Integer.SIZE) == 0L ? 0 :
          (int) ((numberHistory & Integer.MAX_VALUE) - (numberHistory >> Integer.SIZE));

      // shift the lowest 32 bits into the highest 32 bits
      // then put i into the lowest 32 bits
      history[number] = (history[number] << Integer.SIZE) | i;
    }

    return number;
  }
}
