package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;

public class Day2 extends AdventSolution2019<Long, Integer> {
  public static void main(String[] args) {
    new Day2().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) {
    return new IntcodeVM(reader).execute(12, 2, IntcodeVM.ExecMode.STATELESS);
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var vm = new IntcodeVM(reader);

    for (var noun = 0; noun < 100; noun++) {
      for (var verb = 0; verb < 100; verb++) {
        if (vm.execute(noun, verb, IntcodeVM.ExecMode.STATELESS) == 19690720) {
          return 100 * noun + verb;
        }
      }
    }

    throw new IllegalArgumentException();
  }
}
