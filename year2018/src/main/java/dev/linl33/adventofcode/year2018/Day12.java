package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.MathUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class Day12 extends AdventSolution2018<Integer, Long> {
  private static final int PART_1_ROUNDS = 20;
  private static final long PART_2_ROUNDS = 50000000000L;

  public static void main(String[] args) {
    new Day12().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws IOException {
    String initState = reader.readLine().split(": ")[1];
    reader.readLine();
    Map<String, String> rules = reader
        .lines()
        .map(line -> line.split(" => "))
        .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));

    String padding = "............................................................";
    initState = padding + initState + padding;

    for (int i = 0; i < PART_1_ROUNDS; i++) {
      initState = simulateSingleRound(initState, rules);
    }

    return sum(initState, padding.length());
  }

  @Override
  public Long part2(BufferedReader reader) throws IOException {
    var initState = reader.readLine().split(": ")[1];
    reader.readLine();
    var rules = reader
        .lines()
        .map(line -> line.split(" => "))
        .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));

    var padding = "................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................";
    var paddingLength = padding.length();
    initState = padding + initState + padding;

    var popChange = 0;
    var newPopDelta = -1;
    var lastPop = 0;

    // reaches steady state after a while, solved by regression
    for (int i = 0; ; i++) {
      initState = simulateSingleRound(initState, rules);
      var newPop = sum(initState, paddingLength);

      newPopDelta = newPop - lastPop;

      if (popChange == newPopDelta) {
        var regression = MathUtil.makeLine(new Point2D(i, lastPop), new Point2D(i + 1, newPop));
        return regression.x() + regression.y() * PART_2_ROUNDS;
      }

      popChange = newPopDelta;
      lastPop = newPop;
    }
  }

  private static String simulateSingleRound(String initState, Map<String, String> rules) {
    var stateSb = new StringBuilder();
    stateSb.append("..");

    var iterationLimit = initState.length() - 5 + 1;
    for (int i = 0; i < iterationLimit; i++) {
      var potState = rules.getOrDefault(initState.substring(i, i + 5), ".");
      stateSb.append(potState);
    }

    stateSb.append("..");

    return stateSb.toString();
  }

  private static int sum(String state, int paddingLength) {
    var sum = 0;
    for (int i = 0; i < state.length(); i++) {
      if (state.charAt(i) == '#') {
        sum += i - paddingLength;
      }
    }

    return sum;
  }
}
