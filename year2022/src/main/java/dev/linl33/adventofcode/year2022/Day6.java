package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;

public class Day6 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws IOException {
    return findMarker(reader, 4);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws IOException {
    return findMarker(reader, 14);
  }

  private static int findMarker(@NotNull BufferedReader reader, int nDistinct) throws IOException {
    var line = reader.readLine();

    for (int i = nDistinct - 1; i < line.length(); i++) {
      if (checkIndex(line, i, nDistinct)) {
        return i + 1;
      }
    }

    throw new IllegalArgumentException();
  }

  private static boolean checkIndex(String line, int fromIndex, int nDistinct) {
    var characterMask = 0;

    var i = 0;
    for (; i < nDistinct; i++) {
      var c = line.codePointAt(fromIndex - i);
      if ((characterMask & (1 << c)) > 0) {
        return false;
      }

      characterMask |= 1 << c;
    }

    return i == nDistinct;
  }
}
