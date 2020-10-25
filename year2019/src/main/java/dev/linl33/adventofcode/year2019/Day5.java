package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;

public class Day5 extends AdventSolution2019<Long, Long> {
  public static void main(String[] args) {
    new Day5().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) {
    return new IntcodeVM(reader)
        .execute(1L, IntcodeVM.ExecMode.STATELESS)
        .getOutput()
        .getLast();
  }

  @Override
  public Long part2(BufferedReader reader) {
    return new IntcodeVM(reader)
        .execute(5L, IntcodeVM.ExecMode.STATELESS)
        .getOutput()
        .getLast();
  }
}
