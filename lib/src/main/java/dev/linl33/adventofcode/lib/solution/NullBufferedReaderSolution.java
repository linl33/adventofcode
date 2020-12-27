package dev.linl33.adventofcode.lib.solution;

import java.io.BufferedReader;

public interface NullBufferedReaderSolution<T1, T2> extends BufferedReaderAdventSolution<T1, T2> {
  @Override
  default T1 part1(BufferedReader reader) throws Exception {
    return null;
  }

  @Override
  default T2 part2(BufferedReader reader) throws Exception {
    return null;
  }
}
