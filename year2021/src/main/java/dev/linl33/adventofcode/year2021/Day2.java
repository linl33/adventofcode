package dev.linl33.adventofcode.year2021;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.function.IntBinaryOperator;

public class Day2 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day2().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var commands = reader
        .lines()
        .map(SubmarineCommand::parse)
        .toArray(SubmarineCommand[]::new);

    return solve(commands, (distance, __) -> distance);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var commands = reader
        .lines()
        .map(SubmarineCommand::parse)
        .toArray(SubmarineCommand[]::new);

    return solve(commands, (distance, horizontal) -> distance * horizontal);
  }

  private static int solve(@NotNull SubmarineCommand[] commands, @NotNull IntBinaryOperator depthOperator) {
    var horizontal = 0;
    var depth = 0;

    for (int i = commands.length - 1; i >= 0; i--) {
      var command = commands[i];
      var distance = command.distance();

      switch (command.direction()) {
        case 'f' -> horizontal += distance;
        case 'd' -> depth += depthOperator.applyAsInt(distance, horizontal);
        case 'u' -> depth -= depthOperator.applyAsInt(distance, horizontal);
      }
    }

    return horizontal * depth;
  }

  private record SubmarineCommand(char direction, int distance) {
    public static SubmarineCommand parse(@NotNull String input) {
      return new SubmarineCommand(
          input.charAt(0),
          input.charAt(input.length() - 1) - '0'
      );
    }
  }
}
