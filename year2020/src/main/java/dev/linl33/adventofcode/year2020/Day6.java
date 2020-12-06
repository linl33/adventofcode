package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.util.AdventUtil;

import java.io.BufferedReader;
import java.util.stream.IntStream;

public class Day6 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return (int) AdventUtil
        .readInputAsGroups(reader)
        .map(stream -> stream.flatMapToInt(String::chars).distinct())
        .mapToLong(IntStream::count)
        .sum();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return AdventUtil
        .readInputAsGroups(reader)
        .mapToInt(stream -> stream
            .mapToInt(str -> str
                .chars()
                .map(i -> 1 << (i - 'a'))
                .sum()
            )
            .reduce(Integer.MAX_VALUE, (a, b) -> a & b)
        )
        .map(Integer::bitCount)
        .sum();
  }
}
