package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.function.BiIntConsumer;
import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.lib.util.PrintUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class Day10 extends AdventSolution2022<Integer, boolean[]>
    implements ByteBufferAdventSolution<Integer, boolean[]>, NullBufferedReaderSolution<Integer, boolean[]> {
  private static final int SCREEN_WIDTH = 40;
  private static final int SCREEN_HEIGHT = 6;

  public static void main(String[] args) {
    new Day10().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public boolean[] part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Object part2PrintMapping(boolean[] part2Result) {
    if (part2Result == null) {
      return null;
    }

    var screen = new char[SCREEN_HEIGHT][SCREEN_WIDTH];

    for (int y = 0, screenPointer = 0; y < SCREEN_HEIGHT; y++) {
      var row = (screen[y] = new char[SCREEN_WIDTH]);

      for (int x = 0; x < SCREEN_WIDTH; x++, screenPointer++) {
        row[x] = part2Result[screenPointer] ? PrintUtil.FULL_BLOCK : PrintUtil.LIGHT_SHADE;
      }
    }

    return screen;
  }

  @Override
  public Integer part1(@NotNull ByteBuffer byteBuffer) throws Exception {
    var sum = new int[1];
    executeCrtProgram(byteBuffer, (cycle, r) -> {
      if (cycle % 40 == 20) {
        sum[0] += r * cycle;
      }
    });

    return sum[0];
  }

  @Override
  public boolean[] part2(@NotNull ByteBuffer byteBuffer) throws Exception {
    var screen = new boolean[SCREEN_WIDTH * SCREEN_HEIGHT];
    executeCrtProgram(byteBuffer, (cycle, r) -> screen[cycle - 1] = Math.abs(((cycle - 1) % 40) - r) < 2);

    return screen;
  }

  private static void executeCrtProgram(@NotNull ByteBuffer byteBuffer, BiIntConsumer onTick) {
    var register = 1;
    var currByte = (byte) 0;

    for (int cycle = 1; cycle <= SCREEN_WIDTH * SCREEN_HEIGHT; cycle++) {
      onTick.accept(cycle, register);

      currByte = byteBuffer.get();
      if (currByte == 'n' || currByte == 'a') {
        byteBuffer.position(byteBuffer.position() + 4);
      } else {
        int multiplier;
        var operand = 0;
        if (currByte == '-') {
          multiplier = -1;
          currByte = byteBuffer.get();
        } else {
          multiplier = 1;
        }

        do {
          operand = 10 * operand + (currByte - '0');
        } while ((currByte = byteBuffer.get()) != '\n');
        register += multiplier * operand;
      }
    }
  }
}
