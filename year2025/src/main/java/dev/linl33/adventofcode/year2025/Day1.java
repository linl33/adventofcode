package dev.linl33.adventofcode.year2025;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day1 extends AdventSolution2025<Integer, Integer> {
  private static final int DIAL_INITIAL = 50;

  static void main() {
    new Day1().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var dialPos = DIAL_INITIAL;
    var zeroCounter = 0;

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var clicks = Integer.parseInt(line, 1, line.length(), 10);
      if (line.charAt(0) == 'L') {
        clicks = -clicks;
      }

      dialPos += clicks;
      if (dialPos % 100 == 0) {
        zeroCounter++;
      }
    }

    return zeroCounter;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var dialPos = DIAL_INITIAL;
    var zeroCounter = 0;

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var clicks = Integer.parseInt(line, 1, line.length(), 10);
      if (line.charAt(0) == 'L') {
        clicks = -clicks;
      }

      var dialPosNext = dialPos + clicks;
      zeroCounter += Math.abs(dialPosNext / 100);
      if (dialPosNext <= 0) {
        zeroCounter += Integer.signum(dialPos);
      }

      dialPos = Math.floorMod(dialPosNext, 100);
    }

    return zeroCounter;
  }
}
