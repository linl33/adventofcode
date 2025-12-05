package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.nio.ByteBuffer;

public class Day5 extends AdventSolution2025<Long, Long> implements ByteBufferAdventSolution<Long, Long> {
  private static final VectorSpecies<Long> LONG_SPECIES = LongVector.SPECIES_MAX;

  static void main() {
    new Day5().runAndPrintAll();
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
    final var freshRanges = new long[1024];
    final var freshRangesCount = parseFreshIdRanges(byteBuffer, freshRanges);

    final var availableIds = new long[1024];
    var availableIdsCount = 0;
    var idNext = 0L;
    while (byteBuffer.hasRemaining()) {
      final var c = byteBuffer.get();
      if (c < '0') {
        availableIds[availableIdsCount++] = idNext;
        idNext = 0L;
      } else {
        idNext = idNext * 10 + (c - '0');
      }
    }

    var count = 0L;
    final var loopBound = Math.ceilDiv(availableIdsCount, LONG_SPECIES.length()) * LONG_SPECIES.length();
    for (var i = 0; i < loopBound; i += LONG_SPECIES.length()) {
      // assume that 0 is not in availableIds
      final var idVec = LongVector.fromArray(LONG_SPECIES, availableIds, i);
      var withinRangeMask = LONG_SPECIES.maskAll(false);

      for (var j = 0; j < freshRangesCount; j += 2) {
        final var lo = LONG_SPECIES.broadcast(freshRanges[j] - 1);
        final var hi = LONG_SPECIES.broadcast(freshRanges[j + 1] + 1);

        withinRangeMask = withinRangeMask.or(
          idVec
            .compare(VectorOperators.GT, lo)
            .and(idVec.compare(VectorOperators.LT, hi))
        );
      }

      count += withinRangeMask.trueCount();
    }

    return count;
  }

  @Override
  public Long part2(@NotNull ByteBuffer byteBuffer) {
    final var freshRanges = new long[1024];
    final var freshRangesCount = parseFreshIdRanges(byteBuffer, freshRanges);

    long count = freshRangesCount / 2;
    for (var i = 0; i < freshRangesCount; i += 2) {
      final var lo = freshRanges[i];
      final var hi = freshRanges[i + 1];
      count += hi - lo;
    }

    return count;
  }

  private static int parseFreshIdRanges(final ByteBuffer byteBuffer, final long[] freshRanges) {
    var rangeCount = 0;
    var rangeNext = 0L;
    while (byteBuffer.hasRemaining()) {
      final var c = byteBuffer.get();
      if (c < '0') {
        if (rangeNext == 0L) {
          // double \n detected, end of first section
          break;
        }

        freshRanges[rangeCount++] = rangeNext;
        rangeNext = 0L;
      } else {
        rangeNext = rangeNext * 10 + (c - '0');
      }
    }

    var queueSize = rangeCount;
    for (int round = 0; round < rangeCount / 2 - 1; round++) {
      var mergedRangesCount = 0;

      loop:
      for (var i = 0; i < queueSize; i += 2) {
        final var lo = freshRanges[i];
        final var hi = freshRanges[i + 1];

        for (var j = i + 2; j < queueSize; j += 2) {
          final var lo2 = freshRanges[j];
          final var hi2 = freshRanges[j + 1];
          final var loMax = Math.max(lo, lo2);
          final var hiMin = Math.min(hi, hi2);
          if (loMax <= hiMin) {
            // ranges before j cannot be merged with i
            // store merged ij to position j
            freshRanges[j] = loMax ^ lo ^ lo2;
            freshRanges[j + 1] = hiMin ^ hi ^ hi2;
            continue loop;
          }
        }

        // this range couldn't be merged
        // note that mergedRangesCount cannot grow faster than i
        freshRanges[mergedRangesCount++] = lo;
        freshRanges[mergedRangesCount++] = hi;
      }

      if (mergedRangesCount == queueSize) {
        break;
      }

      queueSize = mergedRangesCount;
    }

    return queueSize;
  }
}
