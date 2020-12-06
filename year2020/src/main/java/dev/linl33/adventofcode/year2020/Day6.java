package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.util.AdventUtil;

import java.io.BufferedReader;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;

public class Day6 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return (int) AdventUtil
        .readInputGrouped(reader)
        .map(stream -> stream.flatMapToInt(String::chars).distinct())
        .mapToLong(IntStream::count)
        .sum();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return solveInternal(reader, (a, b) -> a & b, ~0);
  }

  public int part1ByLogicalOr(BufferedReader reader) {
    return solveInternal(reader, (a, b) -> a | b, 0);
  }

  private static int solveInternal(BufferedReader reader, IntBinaryOperator reducer, int identity) {
    return AdventUtil
        .readInputGrouped(reader)
        .mapToInt(stream -> stream
            .mapToInt(str -> str
                .chars()
                .reduce(0, (a, b) -> a | (1 << (b - 'a')))
            )
            .reduce(identity, reducer)
        )
        .map(Integer::bitCount)
        .sum();
  }
}
