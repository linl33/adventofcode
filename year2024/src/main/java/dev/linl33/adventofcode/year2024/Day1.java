package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;

public class Day1 extends AdventSolution2024<Integer, Integer> {
  private static final int SEPARATOR_LENGTH = 3;

  public static void main() {
    new Day1().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var numberLength = (lines[0].length() - SEPARATOR_LENGTH) / 2;

    var listA = new ArrayList<Integer>();
    var listB = new ArrayList<Integer>();

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var left = Integer.parseInt(line, 0, numberLength, 10);
      var right = Integer.parseInt(line, numberLength + SEPARATOR_LENGTH, line.length(), 10);

      listA.add(left);
      listB.add(right);
    }

    Collections.sort(listA);
    Collections.sort(listB);

    var total = 0;
    for (var i = 0; i < lines.length; i++) {
      total += Math.abs(listA.get(i) - listB.get(i));
    }

    return total;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var numberLength = (lines[0].length() - SEPARATOR_LENGTH) / 2;

    var listA = new ArrayList<Integer>();
    var listB = new ArrayList<Integer>();

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var left = Integer.parseInt(line, 0, numberLength, 10);
      var right = Integer.parseInt(line, numberLength + SEPARATOR_LENGTH, line.length(), 10);

      listA.add(left);
      listB.add(right);
    }

    Collections.sort(listA);

    var freqMap = AdventUtil.buildFreqMap(listB);

    var total = 0;
    for (var i = 0; i < lines.length; i++) {
      var a = listA.get(i);
      var freq = freqMap.getOrDefault(a, 0L);

      total += freq * a;
    }

    return total;
  }
}
