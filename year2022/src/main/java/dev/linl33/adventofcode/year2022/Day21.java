package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.function.LongSupplier;

public class Day21 extends AdventSolution2022<Long, Long> {
  private static final String HUMAN_NODE = "humn";
  private static final String ROOT_NODE = "root";

  public static void main(String[] args) {
    new Day21().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);
    var monkeys = new HashMap<String, LongSupplier>(lines.length);

    for (var line : lines) {
      var key = line.substring(0, 4);

      LongSupplier func;
      if (line.length() < 10) {
        var val = Integer.parseInt(line, 6, line.length(), 10);
        func = () -> val;
      } else {
        var left = line.substring(6, 10);
        var right = line.substring(13, 17);
        var operator = line.codePointAt(11);

        func = switch (operator) {
          case '+' -> () -> monkeys.get(left).getAsLong() + monkeys.get(right).getAsLong();
          case '-' -> () -> monkeys.get(left).getAsLong() - monkeys.get(right).getAsLong();
          case '*' -> () -> monkeys.get(left).getAsLong() * monkeys.get(right).getAsLong();
          case '/' -> () -> monkeys.get(left).getAsLong() / monkeys.get(right).getAsLong();
          default -> throw new IllegalArgumentException();
        };
      }

      monkeys.put(key, func);
    }

    return monkeys.get(ROOT_NODE).getAsLong();
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var monkeys = new HashMap<String, LongSupplier>(lines.length);
    var operands = new HashMap<String, String[]>(lines.length);
    var operators = new HashMap<String, Integer>(lines.length);

    for (var line : lines) {
      var key = line.substring(0, 4);

      LongSupplier func;
      if (line.length() < 10) {
        var val = Integer.parseInt(line, 6, line.length(), 10);
        func = () -> val;
      } else {
        var left = line.substring(6, 10);
        var right = line.substring(13, 17);
        var operator = line.codePointAt(11);

        operands.put(key, new String[] { left, right });
        operators.put(key, operator);

        func = switch (operator) {
          case '+' -> () -> monkeys.get(left).getAsLong() + monkeys.get(right).getAsLong();
          case '-' -> () -> monkeys.get(left).getAsLong() - monkeys.get(right).getAsLong();
          case '*' -> () -> monkeys.get(left).getAsLong() * monkeys.get(right).getAsLong();
          case '/' -> () -> monkeys.get(left).getAsLong() / monkeys.get(right).getAsLong();
          default -> throw new IllegalArgumentException();
        };
      }

      monkeys.put(key, func);
    }

    var humanValAccessed = new boolean[] { false };
    monkeys.put(HUMAN_NODE, () -> {
      humanValAccessed[0] = true;
      return 1L;
    });

    var humanNumber = 1L;
    var curr = ROOT_NODE;
    while (!curr.equals(HUMAN_NODE)) {
      var ops = operands.get(curr);
      var leftOp = ops[0];
      var rightOp = ops[1];

      var leftFunc = monkeys.get(leftOp);
      var rightFunc = monkeys.get(rightOp);

      var leftVal = leftFunc.getAsLong();
      var leftIsUnknown = humanValAccessed[0];
      var rightVal = rightFunc.getAsLong();
      humanValAccessed[0] = false;

      //noinspection StringEquality
      if (curr == ROOT_NODE) {
        humanNumber = leftIsUnknown ? rightVal : leftVal;
      } else {
        int operator = operators.get(curr);

        if (leftIsUnknown) {
          humanNumber = switch (operator) {
            case '+' -> humanNumber - rightVal;
            case '-' -> humanNumber + rightVal;
            case '*' -> humanNumber / rightVal;
            case '/' -> humanNumber * rightVal;
            default -> throw new IllegalArgumentException();
          };
        } else {
          humanNumber = switch (operator) {
            case '+' -> humanNumber - leftVal;
            case '-' -> leftVal - humanNumber;
            case '*' -> humanNumber / leftVal;
            case '/' -> leftVal / humanNumber;
            default -> throw new IllegalArgumentException();
          };
        }
      }

      curr = leftIsUnknown ? leftOp : rightOp;
    }

    return humanNumber;
  }
}
