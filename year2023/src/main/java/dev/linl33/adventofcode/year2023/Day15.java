package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Day15 extends AdventSolution2023<Integer, Integer> implements ByteBufferAdventSolution<Integer, Integer> {
  private static final Pattern STEP_DELIM_PATTERN = Pattern.compile("[=\\-]");

  public static void main(String[] args) {
    new Day15().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws IOException {
    return Arrays.stream(reader.readLine().split(",")).mapToInt(Day15::hash).sum();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws IOException {
    var steps = reader.readLine().split(",");
    var boxes = new HashMap<Integer, Map<String, Integer>>();

    for (var step : steps) {
      var parts = STEP_DELIM_PATTERN.splitWithDelimiters(step, 2);
      var label = parts[0];
      var operation = parts[1].codePointAt(0);
      var focalLength = parts[2];

      var boxId = hash(label);
      if (operation == '=') {
        boxes.putIfAbsent(boxId, new LinkedHashMap<>());
        boxes.get(boxId).put(label, Integer.parseInt(focalLength));
      } else {
        if (boxes.containsKey(boxId)) {
          boxes.get(boxId).remove(label);
        }
      }
    }

    var sum = 0;
    for (var kv : boxes.entrySet()) {
      var boxOrder = kv.getKey() + 1;

      var focalLength = kv.getValue().values().toArray(Integer[]::new);
      for (int i = 0; i < focalLength.length; i++) {
        sum += boxOrder * (i + 1) * focalLength[i];
      }
    }

    return sum;
  }

  @Override
  public Integer part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Integer part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return super.part2(identifier);
  }

  @Override
  public Integer part1(@NotNull ByteBuffer byteBuffer) {
    var memSeg = MemorySegment.ofBuffer(byteBuffer);

    var offset = 0L;
    var sum = 0L;

    var readLimit = memSeg.byteSize() / 8 * 8 - 1;
    while (offset < readLimit) {
      var next = memSeg.get(ValueLayout.JAVA_LONG_UNALIGNED, offset);

      // for every byte <= ',', set its most significant bit to 1
      // adapted from https://dotat.at/@/2022-06-27-tolower-swar.html
      var separatorMask = (~next ^ (next + 0x5353535353535353L)) & 0x8080808080808080L;
      var inputLength = Long.numberOfTrailingZeros(separatorMask) / 8;
      offset += inputLength + 1;

      var paddedInput = next << (8 - inputLength) * 8;

      // for 0x0807060504030201
      // calculate 0x01 * 0x81 + 0x03 * 0x61 + 0x05 * 0x41 + 0x07 * 0x61
      // note that 0x81 = 17^8 mod 256, 0x71 = 17^7 mod 256, 0x61 = 17^6 mod 256, etc.
      var evenBytes = paddedInput & 0x00ff00ff00ff00ffL;
      var evenSum = (evenBytes * 0x0081006100410021L) >> 48;

      // calculate 0x02 * 0x71 + 0x04 * 0x51 + 0x06 * 0x31 + 0x08 * 0x11
      var oddBytes = (paddedInput >> 8) & 0x00ff00ff00ff00ffL;
      var oddSum = (oddBytes * 0x0071005100310011L) >> 48;

      sum += (evenSum + oddSum) & 0xffL;
    }

    // TODO: this doesn't handle the case where the remaining bytes contain more than 1 term

    var finalTerm = 0;

    var remainingBytes = (int) (memSeg.byteSize() - offset - 1);
    for (int i = 0; i < remainingBytes; i++) {
      finalTerm += memSeg.get(ValueLayout.JAVA_BYTE, offset + i) * ((remainingBytes - i) * 0x10 + 1);
    }
    sum += finalTerm & 0xffL;

    return (int) sum;
  }

  @Override
  public Integer part2(@NotNull ByteBuffer byteBuffer) throws Exception {
    return -1;
  }

  private static int hash(String input) {
    return (int) (input.codePoints().mapToLong(i -> i).reduce(0, (acc, curr) -> (acc + curr) * 17) & 0xffL);
  }
}
