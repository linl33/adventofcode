package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day6 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws IOException {
    var timeArr = Arrays.stream(reader.readLine().split("\\s+"))
        .skip(1)
        .mapToInt(Integer::parseInt)
        .toArray();
    var distanceArr = Arrays.stream(reader.readLine().split("\\s+"))
        .skip(1)
        .mapToLong(Long::parseLong)
        .toArray();

    return IntStream
        .range(0, timeArr.length)
        .map(i -> countWaysToWin(timeArr[i], distanceArr[i]))
        .reduce(1, (left, right) -> left * right);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws IOException {
    int time = Arrays.stream(reader.readLine().split("\\s+"))
        .skip(1)
        .collect(Collectors.collectingAndThen(Collectors.joining(), Integer::parseInt));
    long distance = Arrays.stream(reader.readLine().split("\\s+"))
        .skip(1)
        .collect(Collectors.collectingAndThen(Collectors.joining(), Long::parseLong));

    return countWaysToWin(time, distance);
  }

  private static int countWaysToWin(int time, long distanceRecord) {
    // equation is 0 = t * (T - t) - D
    // where
    //   T is total time
    //   t is charging time
    //   D is the record to beat
    var discriminant = Math.sqrt(Math.multiplyFull(time, time) - 4 * distanceRecord);
    var right = (int) Math.ceil((discriminant + time) / 2) - 1;
    var left = (int) ((time - discriminant) / 2) + 1;

    // number of integers between left and right (inclusive)
    // alternatively, by parabola symmetry (time / 2 - left + 1) * 2 - (1 - time % 2)
    return right - left + 1;
  }
}
