package dev.linl33.adventofcode.year2025;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class Day4 extends AdventSolution2025<Integer, Integer> implements ByteBufferAdventSolution<Integer, Integer>, NullBufferedReaderSolution<Integer, Integer> {
  static void main() {
    new Day4().runAndPrintAll();
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
    return countRemovablePaperRolls(byteBuffer, 1);
  }

  @Override
  public Integer part2(@NotNull ByteBuffer byteBuffer) {
    return countRemovablePaperRolls(byteBuffer, Integer.MAX_VALUE);
  }

  private static int countRemovablePaperRolls(final ByteBuffer byteBuffer, final int maxRounds) {
    final var dim = ((int) Math.sqrt(4 * byteBuffer.limit() + 1) - 1) / 2;

    // plus 2 to add an extra row and columns at the start and end
    final var alignedWidth = 1 << (Integer.SIZE - Integer.numberOfLeadingZeros(dim + 2 - 1));
    final var grid = new byte[(dim + 2) * alignedWidth];
    final var queue = new int[dim * dim * 2];
    var queuePointer = buildGrid(byteBuffer, dim, alignedWidth, grid, queue);

    var firstRemovedPos = queue[0];
    var lastRemovedPos = queue[queuePointer - 1];

    var count = 0;
    for (int i = 0; i < maxRounds; i++) {
      final var loToRemove = firstRemovedPos - alignedWidth - 1;
      var loToRemoveIdx = 0;
      for (var idx = 0; idx < queuePointer; idx++) {
        final var pos = queue[idx];
        if (pos >= loToRemove) {
          loToRemoveIdx = idx;
          break;
        }
      }

      final var hiToRemove = lastRemovedPos + alignedWidth + 1;
      var hiToRemoveIdx = queuePointer - 1;
      for (var idx = queuePointer - 1; idx >= 0; idx--) {
        final var pos = queue[idx];
        if (pos <= hiToRemove) {
          hiToRemoveIdx = idx;
          break;
        }
      }

      var queuePointerNext = loToRemoveIdx;
      var rmQueuePointer = queue.length - 1;

      for (var j = loToRemoveIdx; j <= hiToRemoveIdx; j++) {
        final var pos = queue[j];
        if (grid[pos] < 4) {
          queue[rmQueuePointer--] = pos;
        } else {
          queue[queuePointerNext++] = pos;
        }
      }

      // everything above hiToRemoveIdx cannot be removed
      System.arraycopy(queue, hiToRemoveIdx + 1, queue, queuePointerNext, queuePointer - (hiToRemoveIdx + 1));
      queuePointerNext += queuePointer - (hiToRemoveIdx + 1);

      final var roundCount = queuePointer - queuePointerNext;
      if (roundCount == 0) {
        break;
      }

      count += roundCount;
      queuePointer = queuePointerNext;

      firstRemovedPos = queue[queue.length - 1];
      lastRemovedPos = queue[rmQueuePointer + 1];

      // adjust neighbors of removed paper rolls
      for (var j = queue.length - 1; j > rmQueuePointer; j--) {
        final var pos = queue[j];
        final var w = Integer.numberOfTrailingZeros(alignedWidth);
        final var gridRow = pos >>> w;
        final var gridCol = pos & ((1 << w) - 1);
        updateGrid(grid, gridRow, gridCol, alignedWidth, (byte) -1);
      }
    }

    return count;
  }

  // parse input into a grid where each cell stores the number of neighbors it has
  private static int buildGrid(final ByteBuffer byteBuffer, final int dim, final int alignedWidth, final byte[] grid, final int[] queue) {
    var queuePointer = 0;

    var pointer = 0;
    for (var y = 1; y <= dim; y++) {
      for (var x = 1; x <= dim; x++, pointer++) {
        final var c = byteBuffer.get(pointer);
        if (c == '@') {
          updateGrid(grid, y, x, alignedWidth, (byte) 1);

          final var w = Integer.numberOfTrailingZeros(alignedWidth);
          queue[queuePointer++] = ((y) << w) + (x);
        }
      }
      pointer++;
    }

    return queuePointer;
  }

  private static void updateGrid(final byte[] grid, final int row, final int col, final int width, final byte delta) {
    final var w = Integer.numberOfTrailingZeros(width);

    grid[((row - 1) << w) + (col - 1)] += delta;
    grid[((row - 1) << w) + col] += delta;
    grid[((row - 1) << w) + (col + 1)] += delta;

    grid[(row << w) + (col - 1)] += delta;
    grid[(row << w) + (col + 1)] += delta;

    grid[((row + 1) << w) + (col - 1)] += delta;
    grid[((row + 1) << w) + col] += delta;
    grid[((row + 1) << w) + (col + 1)] += delta;
  }
}
