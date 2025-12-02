package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.lib.ByteBufferAsCharSequence;
import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.nio.ByteBuffer;

public class Day2 extends AdventSolution2025<Long, Long> implements ByteBufferAdventSolution<Long, Long> {
  private static final long[] TEMPLATES = new long[] {
                     0L,
    111111111111111111L,
   1010101010101010101L,
   1001001001001001001L,
     10001000100010001L,
      1000010000100001L,
   1000001000001000001L,
       100000010000001L,
     10000000100000001L,
   1000000001000000001L,
          100000000001L,
  };

  private static final long[] POWERS_OF_TEN = new long[] {
    1L,
    10L,
    100L,
    1000L,
    10000L,
    100000L,
    1000000L,
    10000000L,
    100000000L,
    1000000000L,
    10000000000L,
    100000000000L,
    1000000000000L,
    10000000000000L,
    100000000000000L,
    1000000000000000L,
    10000000000000000L,
    100000000000000000L,
    1000000000000000000L,
  };

  static void main() {
    new Day2().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Long part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Long part1(@NotNull ByteBuffer byteBuffer) {
    return sumInvalidIds(new ByteBufferAsCharSequence(byteBuffer), 2, Day2::generateTemplate);
  }

  @Override
  public Long part2(@NotNull ByteBuffer byteBuffer) {
    return sumInvalidIds(new ByteBufferAsCharSequence(byteBuffer), Integer.MAX_VALUE, Day2::generateTemplate);
  }

  private static long sumInvalidIds(final CharSequence value, final int sequenceCountMax, final IntToLongBinaryOperator templateGenerator) {
    var invalidIdSum = 0L;

    var startIdx = 0;
    var dashIdx = 0;

    final var valueLength = value.length();
    for (var i = 0; i < valueLength; i++) {
      var ch = value.charAt(i);
      if (ch == '-') {
        dashIdx = i;
      } else if (ch == ',' || ch == '\n') {
        final var lo = Long.parseLong(value, startIdx, dashIdx, 10);
        final var hi = Long.parseLong(value, dashIdx + 1, i, 10);

        invalidIdSum += sumInvalidIdsInRange(lo - 1, hi, sequenceCountMax, templateGenerator);
        startIdx = i + 1;
      }
    }

    return invalidIdSum;
  }

  private static long sumInvalidIdsInRange(final long lo, final long hi, final int sequenceCountMax, final IntToLongBinaryOperator templateGenerator) {
    var invalidIdSum = 0L;
    final var searchLimit = Math.min(sequenceCountMax, countDigits(hi));

    var digits = countDigits(lo);
    var nextDigit = POWERS_OF_TEN[digits];
    var curr = nextInvalidId(lo, digits, searchLimit, templateGenerator, nextDigit);
    while (curr <= hi) {
      invalidIdSum += curr;

      while (curr >= nextDigit) {
        nextDigit = POWERS_OF_TEN[++digits];
      }
      curr = nextInvalidId(curr, digits, searchLimit, templateGenerator, nextDigit);
    }

    return invalidIdSum;
  }

  private static long nextInvalidId(final long curr, final int digits, final int searchLimit, final IntToLongBinaryOperator templateGenerator, final long initialMinInvalidId) {
    var minInvalidId = initialMinInvalidId;

    for (int sequenceCount = 2; sequenceCount <= searchLimit; sequenceCount++) {
      if (digits % sequenceCount != 0) {
        continue;
      }

      final var template = templateGenerator.applyAsLong(digits, sequenceCount);
      minInvalidId = Math.min(minInvalidId, curr / template * template + template);
    }

    if (minInvalidId != initialMinInvalidId) {
      return minInvalidId;
    }

    return nextInvalidId(minInvalidId, digits + 1, searchLimit, templateGenerator, POWERS_OF_TEN[digits + 1]);
  }

  private static long generateTemplate(final int digits, final int sequenceCount) {
    final var width = digits / sequenceCount;
    var template = 0L;
    for (int i = 0, j = 0; i < sequenceCount; i++, j += width) {
      template += POWERS_OF_TEN[j];
    }

    return template;
  }

  private static long lookupTemplate(final int digits, final int sequenceCount) {
    return TEMPLATES[digits / sequenceCount] % POWERS_OF_TEN[digits];
  }

  private static int countDigits(final long value) {
    for (var i = 0; i < POWERS_OF_TEN.length; i++) {
      if (POWERS_OF_TEN[i] > value) {
        return i;
      }
    }

    // should not happen
    return 0;
  }

  @FunctionalInterface
  private interface IntToLongBinaryOperator {
    long applyAsLong(int left, int right);
  }
}
