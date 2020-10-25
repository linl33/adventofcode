package dev.linl33.adventofcode.year2018;

import java.io.BufferedReader;
import java.io.IOException;

public class Day5 extends AdventSolution2018<Integer, Integer> {
  public static void main(String[] args) {
    new Day5().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws IOException {
    return reactPolymer(reader.readLine()).length();
  }

  @Override
  public Integer part2(BufferedReader reader) throws IOException {
    String polymer = reader.readLine();
    String polymerLower = polymer.toLowerCase();

    return polymerLower
        .chars()
        .mapToObj(Character::toString)
        .map(c -> c + "|" + c.toUpperCase())
        .distinct()
        .map(r -> polymer.replaceAll(r, ""))
        .map(Day5::reactPolymer)
        .mapToInt(String::length)
        .min()
        .orElseThrow(IllegalArgumentException::new);
  }

  private static String reactPolymer(String polymer) {
    StringBuilder polymerSb = new StringBuilder(polymer);

    int i = 0;
    while (i != polymerSb.length() - 1 && polymerSb.length() > 1) {
      var curr = polymerSb.charAt(i);
      var next = polymerSb.charAt(i + 1);

      if ((curr - next == 32) || (curr - next == -32)) {
        polymerSb.delete(i, i + 2);
        i = (i == 0) ? i : (i - 1);
      } else {
        i++;
      }
    }

    return polymerSb.toString();
  }
}
