package dev.linl33.adventofcode.year2025;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;

public class Day6 extends AdventSolution2025<Long, Long> {
  static void main() {
    new Day6().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var rowCount = lines.length;
    var rows = new ArrayList<String>();

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];
      var splits = line.split(" ");
      for (int j = 0; j < splits.length; j++) {
        var s = splits[j];
        if (s.isEmpty()) {
          continue;
        }
        rows.add(s);
      }
    }

    var total = 0L;
    var columnCount = rows.size() / rowCount;
    for (var i = 0; i < columnCount; i++) {
      var op = rows.get(i + columnCount * (rowCount - 1));
      var result = Long.parseLong(rows.get(i));
      for (var j = 1; j < rowCount - 1; j++) {
        var num = Long.parseLong(rows.get(i + columnCount * j));
        if (op.equals("+")) {
          result += num;
        } else {
          result *= num;
        }
      }

      total += result;
    }

    return total;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var total = 0L;

    var lastLine = lines[lines.length - 1];
    var numbers = new int[4];
    var numCount = 0;
    var op = '\0';

    for (var pointer = 0; pointer < lastLine.length(); pointer++) {
      var c = lastLine.charAt(pointer);
      if ((c & '\n') == '\n') {
        long sub = numbers[0];
        for (var i = 1; i < numCount; i++) {
          if (op == '+') {
            sub += numbers[i];
          } else {
            sub *= numbers[i];
          }
        }

        total += sub;
        numCount = 0;

        op = c;
      }

      int nextNum = 0;
      for (var i = 0; i < lines.length - 1; i++) {
        var n = lines[i].charAt(pointer);
        if (n < '0') {
          continue;
        }

        nextNum = nextNum * 10 + (n - '0');
      }

      if (nextNum != 0) {
        numbers[numCount++] = nextNum;
      }
    }

    long sub = numbers[0];

    for (var i = 1; i < numCount; i++) {
      if (op == '+') {
        sub += numbers[i];
      } else {
        sub *= numbers[i];
      }
    }

    return total + sub;
  }
}
