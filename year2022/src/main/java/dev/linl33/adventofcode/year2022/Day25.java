package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.List;

public class Day25 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day25().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day25"));
//    new Day25().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day25test1"));

//    System.out.println(convert("10000000000000000000"));
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toList();

    var sum = 0L;
    for (var line : lines) {
//      System.out.println(line);

      var num = convert(line);

//      System.out.println(num);
//      System.out.println();

      sum += num;
    }

    System.out.println(sum);

    var stack = new ArrayDeque<StringBuilder>();
    var stack2 = new ArrayDeque<Integer>();

    var init = new StringBuilder("20000000000000000000");
//    var init = new StringBuilder("000000");

    stack.push(init);
//    stack2.push(0);
    stack2.push(1);

    var i = 0;

    while (!stack.isEmpty()) {
      i++;

      var curr = stack.pop();
      var currVal = convert(curr);

      if (currVal == sum) {
        System.out.println(curr);
        return null;
      }

      if (i % (1 << 18) == 0) {
        System.out.println(i + ": " + (sum - currVal));
      }

      List<Character> choice;
      if (currVal > sum) {
        choice = List.of('0', '-', '=');
      } else {
        choice = List.of('0', '1', '2');
      }

      int currIdx = stack2.pop();
      if (currIdx < curr.length()) {
        for (char nextChar : choice) {
          var next = new StringBuilder(curr);
          next.setCharAt(currIdx, nextChar);
          stack.push(next);
          stack2.push(currIdx + 1);
        }
      }
    }

    return null;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    throw new UnsupportedOperationException();
  }

  private static long convert(CharSequence line) {
    var num = 0L;
    for (int i = 0; i < line.length(); i++) {
      var d = switch (line.charAt(i)) {
        case '2' -> 2;
        case '1' -> 1;
        case '0' -> 0;
        case '-' -> -1;
        case '=' -> -2;
        default -> throw new IllegalArgumentException();
      };

//      num += ((long) Math.pow(5, line.length() - i - 1)) * d;
      num += pow(5, line.length() - i - 1) * d;
    }

    return num;
  }

  private static long pow(int a, int b) {
    var result = 1L;

    for (int i = 0; i < b; i++) {
      result *= a;
    }

    return result;
  }
}
