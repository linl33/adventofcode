package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.LongStream;

public class Day16 extends AdventSolution2021<Long, Long> {
  public static void main(String[] args) {
    new Day16().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws IOException {
    return solve(reader, 0);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws IOException {
    return solve(reader, 1);
  }

  private static long solve(@NotNull BufferedReader reader, int bufferIdx) throws IOException {
    var line = reader.readLine();

    // TODO: try bitset instead of StringBuilder
    var sb = new StringBuilder();
    for (int i = 0; i < line.length(); i++) {
      toHex(line.charAt(i), sb);
    }

    var buffer = new long[2];
    sumVersions(sb, 0, buffer);

    return buffer[bufferIdx];
  }

  private static void toHex(char c, @NotNull StringBuilder sb) {
    var output = Integer.toBinaryString(Integer.parseInt(c + "", 16));
    sb.append("0".repeat(Math.max(0, 4 - output.length())));
    sb.append(output);
  }

  private static int sumVersions(@NotNull CharSequence bin, int pos, @NotNull long[] buffer) {
    // TODO: try a visitor solution, might be cleaner

    var version = Integer.parseInt(bin, pos, pos += 3, 2);
    var packetType = Integer.parseInt(bin, pos, pos += 3, 2);

    var accumulator = LongStream.builder();

    if (packetType == 4) {
      boolean lastLiteral;
      do {
        lastLiteral = bin.charAt(pos) == '0';
        for (int i = 1; i < 5; i++) {
          accumulator.accept(bin.charAt(pos + i));
        }
        pos += 5;
      } while (!lastLiteral);
    } else {
      var lengthTypeId = bin.charAt(pos++);

      if (lengthTypeId == '0') {
        var totalSubPacketLength = Integer.parseInt(bin, pos, pos += 15, 2);
        var stop = pos + totalSubPacketLength;
        do {
          pos = sumVersions(bin, pos, buffer);
          accumulator.accept(buffer[1]);
        } while (pos < stop);
      } else {
        var subPacketCount = Integer.parseInt(bin, pos, pos += 11, 2);
        for (int i = 0; i < subPacketCount; i++) {
          pos = sumVersions(bin, pos, buffer);
          accumulator.accept(buffer[1]);
        }
      }
    }

    buffer[0] += version;

    var stream = accumulator.build();
    buffer[1] = switch (packetType) {
      case 4 -> stream.reduce(0, (result, next) -> (result << 1) | (next - '0'));
      case 0 -> stream.sum();
      case 1 -> stream.reduce((a, b) -> a * b).orElseThrow();
      case 2 -> stream.min().orElseThrow();
      case 3 -> stream.max().orElseThrow();
      case 5 -> stream.reduce((a, b) -> a > b ? 1 : 0).orElseThrow();
      case 6 -> stream.reduce((a, b) -> a < b ? 1 : 0).orElseThrow();
      case 7 -> stream.reduce((a, b) -> a == b ? 1 : 0).orElseThrow();
      default -> throw new IllegalStateException();
    };

    return pos;
  }
}
