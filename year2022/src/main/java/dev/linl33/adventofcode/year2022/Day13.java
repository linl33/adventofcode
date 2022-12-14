package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.VectorIoUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Day13 extends AdventSolution2022<Integer, Integer>
    implements ByteBufferAdventSolution<Integer, Integer>, NullBufferedReaderSolution<Integer, Integer> {
  private static final List<List<Integer>> DIVIDER_A = List.of(List.of(2));
  private static final List<List<Integer>> DIVIDER_B = List.of(List.of(6));

  public static void main(String[] args) {
    new Day13().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Integer part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Integer part1(@NotNull ByteBuffer reader) throws Exception {
    var counters = new int[3];
    var pairLeft = new List<?>[1];

    VectorIoUtil.readLines(reader, line -> {
      var i = (counters[0]++) % 3;

      if (i == 2) {
        return;
      }

      if (i == 1) {
        var parsed = parseLine(line, pairLeft[0].size());
        var order = check(pairLeft[0], parsed);
        var pairIdx = ++counters[1];

        if (order <= 0) {
          counters[2] += pairIdx;
        }
      } else {
        pairLeft[0] = parseLine(line, 0);
      }
    });

    return counters[2];
  }

  @Override
  public Integer part2(@NotNull ByteBuffer reader) throws Exception {
    var counters = new int[3];
    VectorIoUtil.readLines(reader, line -> {
      var i = (counters[0]++) % 3;

      if (i == 2) {
        return;
      }

      var list = parseLine(line, 1);
      if (check(list, DIVIDER_A) < 0) {
        counters[1]++;
      } else if (check(list, DIVIDER_B) < 0) {
        counters[2]++;
      }
    });

    return (counters[1] + 1) * ((counters[1] + 1) + (counters[2] + 1));
  }

  private static int check(Object left, Object right) {
    if (left instanceof Integer leftInt && right instanceof Integer rightInt) {
      return leftInt - rightInt;
    }

    if (left instanceof List<?> leftList && right instanceof List<?> rightList) {
      if (leftList.isEmpty()) {
        return rightList.isEmpty() ? 0 : -1;
      }

      var size = Math.min(leftList.size(), rightList.size());
      for (int i = 0; i < size; i++) {
        var leftItem = leftList.get(i);
        var rightItem = rightList.get(i);

        var itemCheck = check(leftItem, rightItem);
        if (itemCheck != 0) {
          return itemCheck;
        }
      }

      return leftList.size() - rightList.size();
    }

    if (left instanceof Integer leftInt) {
      return check(List.of(leftInt), right);
    }

    if (right instanceof Integer rightInt) {
      return check(left, List.of(rightInt));
    }

    throw new IllegalStateException("Malformed input");
  }

  private static List<Object> parseLine(CharSequence line, int limit) {
    var stack = new ArrayDeque<List<Object>>();

    for (int i = 0; i < line.length(); i++) {
      var c = line.charAt(i);

      if (c == '[') {
        stack.push(new ArrayList<>());
        continue;
      }

      if (limit > 0 && stack.size() == 1 && stack.peek().size() >= limit) {
        return stack.pop();
      }

      if (c == ']') {
        var l = stack.pop();
        if (stack.isEmpty()) {
          return l;
        } else {
          stack.peek().add(l);
        }
      } else if (c != ',') {
        var num = c - '0';

        var next = line.charAt(i + 1);
        if (next >= '0' && next <= '9') {
          i++;
          num = 10 * num + (next - '0');
        }

        stack.peek().add(num);
      }
    }

    throw new IllegalArgumentException("Malformed input");
  }
}
