package dev.linl33.adventofcode.year2020;

import java.io.BufferedReader;
import java.util.OptionalInt;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class Day1 extends AdventSolution2020<Integer, Integer> {
  private static final int SUM_TARGET = 2020;

  public static void main(String[] args) {
    new Day1().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    // assume that inputs are non-negative and less than 2020
    var intBoolArr = new boolean[SUM_TARGET];

    var intArr = reader
        .lines()
        .mapToInt(Integer::parseInt)
        .peek(i -> intBoolArr[i] = true)
        .toArray();

    for (int partial : intArr) {
      if (intBoolArr[SUM_TARGET - partial]) {
        return partial * (SUM_TARGET - partial);
      }
    }

    throw new IllegalArgumentException();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    // assume that inputs are non-negative and less than 2020
    var intBoolArr = new boolean[SUM_TARGET];

    var intArr = reader
        .lines()
        .mapToInt(Integer::parseInt)
        .peek(i -> intBoolArr[i] = true)
        .toArray();

    for (int first = 0; first < intArr.length; first++) {
      for (int second = first + 1; second < intArr.length; second++) {
        var partial = intArr[first] + intArr[second];
        if (partial < SUM_TARGET && intBoolArr[SUM_TARGET - partial]) {
          return intArr[first] * intArr[second] * (SUM_TARGET - partial);
        }
      }
    }

    throw new IllegalArgumentException();
  }

  public int solveByIntStream(BufferedReader reader, int numbers) {
    var intArr = reader
        .lines()
        .mapToInt(Integer::parseInt)
        .toArray();

    return addLayer(numbers, intArr).orElseThrow();
  }

  private static OptionalInt addLayer(int numbers, int[] intArr) {
    IntUnaryOperator lookup = (int i) -> intArr[i];
    return addLayer(0, numbers, intArr.length, lookup, lookup, lookup);
  }

  private static OptionalInt addLayer(int i,
                                      int numbers,
                                      int range,
                                      IntUnaryOperator lookup,
                                      IntUnaryOperator sum,
                                      IntUnaryOperator product) {
    if (numbers == 1) {
      return IntStream
          .range(i, range)
          .filter(last -> sum.applyAsInt(last) == SUM_TARGET)
          .map(product)
          .findFirst();
    }

    return IntStream
        .range(i, range)
        .mapToObj(nextLayer -> addLayer(
            nextLayer + 1,
            numbers - 1,
            range,
            lookup,
            lookup.andThen(newVal -> sum.applyAsInt(nextLayer) + newVal),
            lookup.andThen(newVal -> product.applyAsInt(nextLayer) * newVal)
        ))
        .filter(OptionalInt::isPresent)
        .findFirst()
        .orElseGet(OptionalInt::empty);
  }
}
