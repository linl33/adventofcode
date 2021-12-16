package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;

public class Day16 extends AdventSolution2021<Long, Long> {
  public static void main(String[] args) {
    new Day16().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var line = reader.readLine();
    var sb = new StringBuilder();
    for (int i = 0; i < line.length(); i++) {
      var hexChar = line.charAt(i);
      toHex(hexChar, sb);
    }

    var buffer = new long[2];
    sumVersions(sb, 0, buffer);

    return buffer[0];
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var line = reader.readLine();
    var sb = new StringBuilder();
    for (int i = 0; i < line.length(); i++) {
      var hexChar = line.charAt(i);
      toHex(hexChar, sb);
    }

    var buffer = new long[2];
    sumVersions(sb, 0, buffer);

    return buffer[1];
  }

  private static void toHex(char c, @NotNull StringBuilder sb) {
    var output = Integer.toBinaryString(Integer.parseInt(c + "", 16));
    sb.append("0".repeat(Math.max(0, 4 - output.length())));
    sb.append(output);
  }

  private static int sumVersions(CharSequence bin, int pos, long[] buffer) {
    var version = Integer.parseInt(bin, pos, pos + 3, 2);
    buffer[0] += version;
    pos += 3;
    var packetType = Integer.parseInt(bin, pos, pos + 3, 2);
    pos += 3;

    if (packetType == 4) {
      boolean lastLiteral;
      var literalSb = new StringBuilder();
      do {
        lastLiteral = bin.charAt(pos) == '0';
        literalSb.append(bin, pos + 1, pos + 5);
        pos += 5;
      } while (!lastLiteral);
      buffer[1] = Long.parseLong(literalSb.toString(), 2);
    } else {
      var lengthTypeId = bin.charAt(pos);
      pos += 1;

      var accumulator = new ArrayList<Long>();

      if (lengthTypeId == '0') {
        var totalSubPackLength = Integer.parseInt(bin, pos, pos + 15, 2);
        pos += 15;
        var stop = pos + totalSubPackLength;
        do {
          pos = sumVersions(bin, pos, buffer);
          accumulator.add(buffer[1]);
        } while (pos < stop);
      } else {
        var subPacketCount = Integer.parseInt(bin, pos, pos + 11, 2);
        pos += 11;
        for (int i = 0; i < subPacketCount; i++) {
          pos = sumVersions(bin, pos, buffer);
          accumulator.add(buffer[1]);
        }
      }

      buffer[1] = switch (packetType) {
        case 0 -> accumulator.stream().mapToLong(Long::longValue).sum();
        case 1 -> accumulator.stream().mapToLong(Long::longValue).reduce((a, b) -> a * b).orElseThrow();
        case 2 -> accumulator.stream().mapToLong(Long::longValue).min().orElseThrow();
        case 3 -> accumulator.stream().mapToLong(Long::longValue).max().orElseThrow();
        case 5 -> accumulator.get(0) > accumulator.get(1) ? 1 : 0;
        case 6 -> accumulator.get(0) < accumulator.get(1) ? 1 : 0;
        case 7 -> accumulator.get(0).equals(accumulator.get(1)) ? 1 : 0;
        default -> throw new IllegalStateException();
      };
    }

    return pos;
  }
}
