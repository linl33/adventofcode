package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;
import java.util.stream.IntStream;

public class Day5 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    new Day5().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return parseBoardingPass(reader).max().orElseThrow();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return parseBoardingPass(reader)
        .sorted()
        .reduce((a, b) -> (b - a > 1) ? a : b)
        .orElseThrow() + 1;
  }

  private static IntStream parseBoardingPass(BufferedReader reader) {
    return reader
        .lines()
        .map(line -> line
            .replace('B', '1')
            .replace('F', '0')
            .replace('R', '1')
            .replace('L', '0')
        )
        .mapToInt(binStr -> Integer.valueOf(binStr, 2));
  }
}
