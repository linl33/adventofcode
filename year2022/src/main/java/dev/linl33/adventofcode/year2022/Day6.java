package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class Day6 extends AdventSolution2022<Integer, Integer>
    implements ByteBufferAdventSolution<Integer, Integer>, NullBufferedReaderSolution<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
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
    return findMarker(byteBuffer, 4);
  }

  @Override
  public Integer part2(@NotNull ByteBuffer byteBuffer) {
    return findMarker(byteBuffer, 14);
  }

  private static int findMarker(@NotNull ByteBuffer byteBuffer, int nDistinct) {
    var line = new byte[byteBuffer.limit()];
    byteBuffer.get(0, line, 0, line.length);

    return findMarker(line, nDistinct);
  }

  private static int findMarker(byte[] line, int nDistinct) {
    for (int i = nDistinct - 1; i < line.length - 1; i += 2) {
      var baseMask = constructMask(line, i, nDistinct - 1);
      if (baseMask == 0) {
        continue;
      }

      if ((baseMask & (1 << line[i - (nDistinct - 1)])) == 0) {
        return i + 1;
      }

      // lookahead 1 position
      if ((baseMask & (1 << line[i + 1])) == 0) {
        return i + 2;
      }
    }

    throw new IllegalArgumentException();
  }

  private static int constructMask(byte[] line, int fromIndex, int length) {
    var mask = 0;
    for (int i = 0; i < length; i++) {
      var c = line[fromIndex - i];
      if ((mask & (1 << c)) != 0) {
        return 0;
      }

      mask |= 1 << c;
    }

    return mask;
  }
}
