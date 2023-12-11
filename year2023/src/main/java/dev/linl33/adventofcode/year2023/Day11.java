package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Day11 extends AdventSolution2023<Long, Long>
  implements ByteBufferAdventSolution<Long, Long>, NullBufferedReaderSolution<Long, Long> {
  private static final VectorSpecies<Byte> BYTE_SPECIES = ByteVector.SPECIES_PREFERRED;

  public static void main(String[] args) {
    new Day11().runAndPrintAll();
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
  public Long part1(@NotNull ByteBuffer byteBuffer) {
    return solveVector(byteBuffer, 2);
  }

  @Override
  public Long part2(@NotNull ByteBuffer byteBuffer) {
    return solveVector(byteBuffer, 1_000_000);
  }

  private static long solveVector(ByteBuffer byteBuffer, final long emptySpaceScale) {
    var memSegment = MemorySegment.ofBuffer(byteBuffer);

    var squareDim = (((int) Math.sqrt(4 * (byteBuffer.limit() + 1))) - 1) / 2;
    var alignedSquareDim = Math.ceilDiv(squareDim, BYTE_SPECIES.length()) * BYTE_SPECIES.length();

    var colCounts = Arena.ofAuto().allocate(ValueLayout.JAVA_BYTE, alignedSquareDim);
    var rowCounts = new int[alignedSquareDim];
    var galaxiesCount = 0;

    for (int y = 0; y < squareDim; y++) {
      var rowOffset = (long) y * (squareDim + 1);

      for (int x = 0; x < squareDim; x += BYTE_SPECIES.length()) {
        var row = ByteVector
          .fromMemorySegment(BYTE_SPECIES, memSegment, rowOffset + x, ByteOrder.nativeOrder(), BYTE_SPECIES.indexInRange(x, squareDim))
          .eq((byte) '#');

        ByteVector
          .fromMemorySegment(BYTE_SPECIES, colCounts, x, ByteOrder.nativeOrder())
          .add((byte) 1, row)
          .intoMemorySegment(colCounts, x, ByteOrder.nativeOrder());

        if (row.anyTrue()) {
          var rowPopcnt = row.trueCount();
          galaxiesCount += rowPopcnt;
          rowCounts[y] += rowPopcnt;
        }
      }
    }

    var colPrefix = 0;
    var rowPrefix = 0;

    var emptySpaceAdjust = 0;

    var totalManhattanDistance = 0L;

    for (int i = 0; i < squareDim; i++) {
      var colVal = colCounts.getAtIndex(ValueLayout.JAVA_BYTE, i);
      var rowVal = rowCounts[i];

      // calculate number of ways to cross the ith row/col, add them up
      // (# of galaxies before the ith row/col) * (# of galaxies on or after the ith row/col)
      totalManhattanDistance += colPrefix * (galaxiesCount - colPrefix) + rowPrefix * (galaxiesCount - rowPrefix);

      // calculate number of ways to cross the empty column
      if (colVal == 0) {
        emptySpaceAdjust += colPrefix * (galaxiesCount - colPrefix);
      }

      // calculate number of ways to cross the empty row
      if (rowVal == 0) {
        emptySpaceAdjust += rowPrefix * (galaxiesCount - rowPrefix);
      }

      colPrefix += colVal;
      rowPrefix += rowVal;
    }

    return (emptySpaceScale - 1) * emptySpaceAdjust + totalManhattanDistance;
  }
}
