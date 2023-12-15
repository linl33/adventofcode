package dev.linl33.adventofcode.year2023;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Day15 extends AdventSolution2023<Integer, Integer> {
  public static void main(String[] args) {
    new Day15().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws IOException {
    return Arrays.stream(reader.readLine().split(",")).mapToInt(Day15::hash).sum();
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws IOException {
    var steps = reader.readLine().split(",");
    var boxes = new HashMap<Integer, Map<String, Integer>>();

    for (var step : steps) {
      var parts = step.splitWithDelimiters("[=\\-]", 2);
      var label = parts[0];
      var operation = parts[1].codePointAt(0);
      var focalLength = parts[2];

      var boxId = hash(label);
      if (operation == '=') {
        boxes.putIfAbsent(boxId, new LinkedHashMap<>());
        boxes.get(boxId).put(label, Integer.parseInt(focalLength));
      } else {
        if (boxes.containsKey(boxId)) {
          boxes.get(boxId).remove(label);
        }
      }
    }

    var sum = 0;
    for (var kv : boxes.entrySet()) {
      var boxOrder = kv.getKey() + 1;

      var focalLength = kv.getValue().values().toArray(Integer[]::new);
      for (int i = 0; i < focalLength.length; i++) {
        sum += boxOrder * (i + 1) * focalLength[i];
      }
    }

    return sum;
  }

  private static int hash(String input) {
    return input.codePoints().reduce(0, (acc, curr) -> ((acc + curr) * 17) % 256);
  }
}
