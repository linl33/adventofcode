package dev.linl33.adventofcode.year2018;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 extends AdventSolution2018<Integer, String> {
  public static void main(String[] args) {
    new Day2().runAndPrintAll();
  }

  public Integer part1(BufferedReader reader) {
    List<String> lines = reader
        .lines()
        .collect(Collectors.toList());

    var count2 = 0;
    var count3 = 0;

    for (String line : lines) {
      var charCount = new HashMap<Character, Integer>();

      for (char c : line.toCharArray()) {
        charCount.compute(c, (character, integer) -> integer != null ? integer + 1 : 1);
      }

      if (charCount.containsValue(2)) {
        count2++;
      }

      if (charCount.containsValue(3)) {
        count3++;
      }
    }

    return count2 * count3;
  }

  public String part2(BufferedReader reader) {
    var lines = reader.lines().collect(Collectors.toList());

    for (int i = 0; i < lines.size(); i++) {
      var idLeft = lines.get(i);

      for (int j = i + 1; j < lines.size(); j++) {
        var idRight = lines.get(j);

        if (diffByOne(idLeft, idRight)) {
          return commonStr(idLeft, idRight);
        }
      }
    }

    throw new IllegalArgumentException();
  }

  private static boolean diffByOne(String left, String right) {
    var diffCount = 0;

    for (int i = 0; i < left.length(); i++) {
      var charLeft = left.charAt(i);
      var charRight = right.charAt(i);

      if (charLeft != charRight) {
        diffCount++;

        if (diffCount > 1) {
          return false;
        }
      }
    }

    return diffCount == 1;
  }

  private static String commonStr(String left, String right) {
    StringBuilder output = new StringBuilder();

    for (int i = 0; i < left.length(); i++) {
      var charLeft = left.charAt(i);
      var charRight = right.charAt(i);

      if (charLeft == charRight) {
        output.append(charLeft);
      }
    }

    return output.toString();
  }
}
