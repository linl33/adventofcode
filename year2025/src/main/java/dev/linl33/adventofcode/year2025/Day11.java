package dev.linl33.adventofcode.year2025;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 extends AdventSolution2025<Long, Long> {
  static void main() {
    new Day11().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var wiring = new HashMap<String, List<String>>();

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var sep = line.indexOf(':');
      var device = line.substring(0, sep);
      var outputs = List.of(line.substring(sep + 2).split(" "));

      wiring.put(device, outputs);
    }

    var cache = new HashMap<String, Long>();
    cache.put("out", 1L);
    countPaths(wiring, "you", cache);

    return cache.get("you");
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var wiring = new HashMap<String, List<String>>();

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var sep = line.indexOf(':');
      var device = line.substring(0, sep);
      var outputs = List.of(line.substring(sep + 2).split(" "));

      wiring.put(device, outputs);
    }

    var start = "svr";
    var end = "out";
    var necessary1 = "fft";
    var necessary2 = "dac";

    var cache = new HashMap<String, Long>(wiring.size() * 2);
    cache.put(end, 0L);
    cache.put(necessary2, 1L);
    countPaths(wiring, necessary1, cache);
    var pathCount = cache.get(necessary1);

    // fft -> dac exists xor dac -> fft exists
    // if both exist, then there must be a loop
    // additionally, fft seems to precede dac in all inputs
    if (pathCount == 0) {
      var tmp = necessary1;
      necessary1 = necessary2;
      necessary2 = tmp;

      cache.clear();
      cache.put(end, 0L);
      cache.put(necessary2, 1L);
      countPaths(wiring, necessary1, cache);
      pathCount = cache.get(necessary1);
    }

    cache.clear();
    cache.put(necessary1, pathCount);
    cache.put(necessary2, 0L);
    cache.put(end, 0L);
    countPaths(wiring, start, cache);
    pathCount = cache.get(start);

    cache.clear();
    cache.put(end, pathCount);
    countPaths(wiring, necessary2, cache);
    return cache.get(necessary2);
  }

  private static void countPaths(Map<String, List<String>> wiring, String current, Map<String, Long> cache) {
    // note that the entire graph is cycle-free
    // so no need to track visited nodes

    if (cache.containsKey(current)) {
      return;
    }

    var count = 0L;
    var outputs = wiring.get(current);
    for (var i = 0; i < outputs.size(); i++) {
      var next = outputs.get(i);
      countPaths(wiring, next, cache);
      count += cache.get(next);
    }

    cache.put(current, count);
  }
}
