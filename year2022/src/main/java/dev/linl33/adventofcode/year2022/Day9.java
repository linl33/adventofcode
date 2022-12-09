package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class Day9 extends AdventSolution2022<Integer, Integer> implements ByteBufferAdventSolution<Integer, Integer>, NullBufferedReaderSolution<Integer, Integer> {
  private static final int HASH_TABLE_BUCKET_SIZE = 10;
  private static final int HASH_TABLE_BUCKETS = 2048;

  public static void main(String[] args) {
    new Day9().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Integer part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Integer part1(@NotNull ByteBuffer byteBuffer) {
    return countUniqueTailPositions(byteBuffer, 2);
  }

  @Override
  public Integer part2(@NotNull ByteBuffer byteBuffer) {
    return countUniqueTailPositions(byteBuffer, 10);
  }

  private static int countUniqueTailPositions(@NotNull ByteBuffer byteBuffer, int knots) {
    var positions = new short[knots * 2];

    var visitedTable = new int[HASH_TABLE_BUCKETS * HASH_TABLE_BUCKET_SIZE];
    var visitedTableBucketSizes = new int[HASH_TABLE_BUCKETS];
    visitedTableBucketSizes[0]++;
    var count = 0;

    while (byteBuffer.hasRemaining()) {
      var dir = byteBuffer.get();
      // skip space
      byteBuffer.position(byteBuffer.position() + 1);
      var magnitude = byteBuffer.get() - '0';
      // could be \n or a digit
      var magnitudeNext = byteBuffer.get();

      if (magnitudeNext != '\n') {
        magnitude = magnitude * 10 + (magnitudeNext - '0');
        // skip \n
        byteBuffer.position(byteBuffer.position() + 1);
      }

      switch (dir) {
        case 'R' -> positions[0] += magnitude;
        case 'L' -> positions[0] -= magnitude;
        case 'U' -> positions[knots] -= magnitude;
        case 'D' -> positions[knots] += magnitude;
        default -> throw new IllegalArgumentException();
      }

      moveKnots:
      for (int i = 0; i < magnitude; i++) {
        var prevKnotX = positions[0];
        var prevKnotY = positions[knots];
        for (int x = 1, y = 1 + knots; x < knots; x++, y++) {
          var currKnotX = positions[x];
          var currKnotY = positions[y];

          if (!isValid(prevKnotX, prevKnotY, currKnotX, currKnotY)) {
            prevKnotX = (positions[x] += Integer.signum(prevKnotX - currKnotX));
            prevKnotY = (positions[y] += Integer.signum(prevKnotY - currKnotY));
          } else {
            if (x == 1) {
              // entire rope is valid now
              break moveKnots;
            }

            // all following knots will not move either
            continue moveKnots;
          }
        }

        var tailPosition = positionToInt(positions[knots - 1], positions[knots - 1 + knots]);
        var hashKey = Math.abs(tailPosition % HASH_TABLE_BUCKETS);
        var hashTableIndex = hashKey * HASH_TABLE_BUCKET_SIZE;
        var hashBucketSize = hashTableIndex + visitedTableBucketSizes[hashKey];
        for (; hashTableIndex < hashBucketSize; hashTableIndex++) {
          if (visitedTable[hashTableIndex] == tailPosition) {
            continue moveKnots;
          }
        }

        visitedTable[hashTableIndex] = tailPosition;
        visitedTableBucketSizes[hashKey]++;
        count++;
      }
    }

    return count + 1;
  }

  private static boolean isValid(int prevX, int prevY, int currX, int currY) {
    return Math.abs(prevX - currX) <= 1 && Math.abs(prevY - currY) <= 1;
  }

  private static int positionToInt(short x, short y) {
    @SuppressWarnings("SuspiciousNameCombination")
    var pos = Integer.expand(Short.toUnsignedInt(x), 0b10101010101010101010101010101010)
        | Integer.expand(Short.toUnsignedInt(y), 0b01010101010101010101010101010101);
    return pos;
  }
}
