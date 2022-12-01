package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.PrintUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.Deque;

public class Day18 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
//    new Day18().runAndPrintAll();
    new Day18().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day18test1"));
//    new Day18().print(SolutionPart.PART_2, new ClasspathResourceIdentifier("day18test1"));
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

//    var limit = lines.length;
    var limit = 1;
    for (int i = 0; i < limit; i++) {
      var line = lines[i];

      var stack = new ArrayDeque<Object[]>();

      var level = 0;
      var leftRead = false;
      for (int j = 0; j < line.length(); j++) {
        var c = line.charAt(j);

        if (c == '[') {
          level++;
          stack.push(new Object[2]);
          continue;
        }

        if (c == ']') {
          level--;
          PrintUtil.enhancedPrint(stack.pop());
          continue;
        }

        if (c != ',') {
          var store = stack.peek();

          if (store[0] == null) {
            store[0] = c;
          } else {
            store[1] = c;
          }
        }
      }
    }

    return null;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    return null;
  }
}
