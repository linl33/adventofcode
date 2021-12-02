package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day1 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day1().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    return countIncreasing(reader, 1);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    return countIncreasing(reader, 3);
  }

  private static int countIncreasing(@NotNull BufferedReader reader, int gap) {
    return countIncreasing(
        reader
            .lines()
            .mapToInt(Integer::parseInt)
            .toArray(),
        gap
    );
  }

  private static int countIncreasing(int[] numbers, int distance) {
    var incCounter = 0;

    for (int i = distance; i < numbers.length; i++) {
      var curr = numbers[i];
      var prev = numbers[i - distance];

      incCounter += curr > prev ? 1 : 0;
    }

    return incCounter;
  }
}
