package dev.linl33.adventofcode.year2018;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 extends AdventSolution2018<Integer, Integer> {
  public static void main(String[] args) {
    new Day1().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return reader
        .lines()
        .mapToInt(Integer::parseInt)
        .sum();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    List<String> allLines = reader.lines().collect(Collectors.toList());
    WrappingLinesIterator wrappingIt = new WrappingLinesIterator(allLines);

    var seenFreq = new HashSet<Integer>();
    var currFreq = wrappingIt.next() + wrappingIt.next();
    seenFreq.add(currFreq);
    do {
      currFreq += wrappingIt.next();
    } while (seenFreq.add(currFreq));

    return currFreq;
  }

  private static class WrappingLinesIterator implements Iterator<Integer> {
    private final List<Integer> lines;
    private int curr;

    private WrappingLinesIterator(List<String> lines) {
      this.lines = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
      this.curr = 0;
    }

    @Override
    public boolean hasNext() {
      return true;
    }

    @Override
    public Integer next() {
      if (curr == lines.size()) {
        curr = 0;
      }

      return lines.get(curr++);
    }
  }
}
