package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Day10 extends AdventSolution2021<Integer, Long>
    implements ByteBufferAdventSolution<Integer, Long>, NullBufferedReaderSolution<Integer, Long> {
  private static final int[] LOOKUP = new int[] { 0, 0, '(', '<', 0, '[', 0, '{' };

  public static void main(String[] args) {
    new Day10().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Long part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Integer part1(@NotNull ByteBuffer byteBuffer) throws Exception {
    var sum = 0;
    var stack = new int[120];
    var stackPointer = 0;
    byte next;

    while (byteBuffer.hasRemaining()) {
      if ((next = byteBuffer.get()) == '\n') {
        stackPointer = 0;
      } else if (next == LOOKUP[next >> 4]) {
        stack[stackPointer++] = next;
      } else {
        var top = stack[--stackPointer];
        if ((next - top) < 0 || (next - top) > 2) {
          sum += switch (next) {
            case ')' -> 3;
            case '>' -> 25137;
            case ']' -> 57;
            case '}' -> 1197;
            default -> throw new IllegalArgumentException();
          };

          nextLine(byteBuffer);
          stackPointer = 0;
        }
      }
    }

    return sum;
  }

  @Override
  public Long part2(@NotNull ByteBuffer byteBuffer) throws Exception {
    var stack = new int[120];
    var scores = new long[90];

    var scorePointer = 0;
    var stackPointer = 0;

    byte next;
    while (byteBuffer.hasRemaining()) {
      if ((next = byteBuffer.get()) == '\n') {
        var score = 0L;
        for (--stackPointer; stackPointer >= 0; stackPointer--) {
          score = score * 5 + switch (stack[stackPointer]) {
            case '(' -> 1;
            case '[' -> 2;
            case '{' -> 3;
            case '<' -> 4;
            default -> throw new IllegalArgumentException();
          };
        }

        scores[scorePointer++] = score;
        stackPointer = 0;
      } else if (next == LOOKUP[next >> 4]) {
        stack[stackPointer++] = next;
      } else {
        var top = stack[--stackPointer];
        if ((next - top) < 0 || (next - top) > 2) {
          nextLine(byteBuffer);
          stackPointer = 0;
        }
      }
    }

    Arrays.sort(scores, 0, scorePointer);
    return scores[scorePointer / 2];
  }

  private static void nextLine(@NotNull ByteBuffer byteBuffer) {
    while (byteBuffer.get() != '\n');
  }
}
