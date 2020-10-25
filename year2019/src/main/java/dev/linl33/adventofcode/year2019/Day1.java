package dev.linl33.adventofcode.year2019;

import java.io.BufferedReader;

public class Day1 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
    new Day1().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return reader
        .lines()
        .mapToInt(Integer::parseInt)
        .map(Day1::massToFuel)
        .sum();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return reader
        .lines()
        .mapToInt(Integer::parseInt)
        .map(Day1::massToTotalFuel)
        .sum();
  }

  public static int massToFuel(int mass) {
    return mass / 3 - 2;
  }

  public static int massToTotalFuel(int start) {
    var sum = 0;
    var next = 0;

    do {
      sum += next;

      next = massToFuel(start);
      start = next;
    } while (next > 0);

    return sum;
  }
}
