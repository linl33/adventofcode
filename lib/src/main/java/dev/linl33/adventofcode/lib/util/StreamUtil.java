package dev.linl33.adventofcode.lib.util;

import java.util.stream.IntStream;

public class StreamUtil {
  public static IntStream rangeReverse(int beginInclusive, int endExclusive) {
    if (endExclusive >= beginInclusive) {
      return IntStream.empty();
    }

    return IntStream.iterate(beginInclusive, i -> i > endExclusive, i -> i - 1);
  }

  public static IntStream rangeReverseClosed(int beginInclusive, int endInclusive) {
    if (endInclusive > beginInclusive) {
      return IntStream.empty();
    }

    return IntStream.iterate(beginInclusive, i -> i >= endInclusive, i -> i - 1);
  }
}
