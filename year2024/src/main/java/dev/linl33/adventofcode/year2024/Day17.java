package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;

public class Day17 extends AdventSolution2024<String, Long> {
  public static void main() {
    new Day17().runAndPrintAll();
    new Day17().benchmark(JmhBenchmarkOption.PART_2);
  }

  @Override
  public String part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var prog = lines[4].substring(9).split(",");

    var regA = Long.parseLong(lines[0], 12, lines[0].length(), 10);
    var regB = Long.parseLong(lines[1], 12, lines[1].length(), 10);
    var regC = Long.parseLong(lines[2], 12, lines[2].length(), 10);

    var out = new ArrayList<String>();

    for (var i = 0; i < prog.length; i += 2) {
      var op = prog[i].charAt(0);
      var arg = prog[i + 1].charAt(0);

      long argComboVal;
      if (arg == '0' || arg == '1' || arg == '2' || arg == '3') {
        argComboVal = arg - '0';
      } else if (arg == '4') {
        argComboVal = regA;
      } else if (arg == '5') {
        argComboVal = regB;
      } else if (arg == '6') {
        argComboVal = regC;
      } else {
        throw new IllegalArgumentException("Invalid arg: " + arg);
      }

      long argLitVal = arg - '0';

      if (op == '0') {
        regA = regA >> argComboVal;
      } else if (op == '1') {
        regB = regB ^ argLitVal;
      } else if (op == '2') {
        regB = argComboVal % 8;
      } else if (op == '3') {
        if (regA != 0) {
          i = (int) (argLitVal - 2);
        }
      } else if (op == '4') {
        regB = regB ^ regC;
      } else if (op == '5') {
        out.add("" + (argComboVal % 8));
      } else if (op == '6') {
        regB = regA >> argComboVal;
      } else if (op == '7') {
        regC = regA >> argComboVal;
      } else {
        throw new IllegalArgumentException("Invalid op: " + op);
      }
    }

    return String.join(",", out);
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var program = lines[4];
    var programSize = (program.length() - "Program: ".length()) / 2 + 1;
    var expected = new int[programSize];
    for (var i = 0; i < programSize; i++) {
      expected[i] = program.charAt(i * 2 + 9) - '0';
    }

    return findA(0, expected.length - 1, expected);
  }

  /*
   * Find the lowest value of register A such that the program prints itself.
   *
   * Recursively find the value of A, 3 bits at a time starting from the most significant bits.
   * Assumptions:
   *  1. The last 2 instructions in the program are **out** followed by **jnz 0**
   *  2. There is only 1 out and 1 jnz
   *  3. exactly 3 bits of A are consumed for each out
   */
  private static long findA(long aInit, int d, int[] program) {
    final var start = aInit << 3;
    final var end = (aInit << 3) + (1L << 3);

    for (var aVal = start; aVal < end; aVal++) {
      var a = aVal;
      var b = 0L;
      var c = 0L;

      // TODO: replace hard-coded program with parsed program
      b = (a & 0b111) ^ 0b101;
      c = a >>> b;
//      a = a >>> 3;
      b = b ^ c ^ 0b110;

      var out = b & 0b111;
      if (out == program[d]) {
        if (d == 0) {
          return aVal;
        }

        var solution = findA(aVal, d - 1, program);
        if (solution != -1L) {
          return solution;
        }
      }
    }

    return -1L;
  }
}
