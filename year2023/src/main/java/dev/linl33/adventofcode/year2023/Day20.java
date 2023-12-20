package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.lib.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class Day20 extends AdventSolution2023<Long, Long> {
  public static void main(String[] args) {
    new Day20().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var moduleState = new HashMap<String, Map<String, String>>();
    var moduleType = new HashMap<String, Integer>();
    var cables = new HashMap<String, List<String>>();
    var queue = new ArrayDeque<Signal>();

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];

      var split = line.substring(1).split(" -> ");
      var name = split[0];
      moduleState.put(name, new HashMap<>());

      var to = split[1].split(", ");
      cables.put(name, List.of(to));

      var type = line.codePointAt(0);
      moduleType.put(name, type);
    }

    cables.forEach((from, to) -> to.forEach(output -> {
      if (!moduleType.containsKey(output)) {
        return;
      }

      if (moduleType.get(output) == ((int) '%')) {
        return;
      }

      moduleState.get(output).put(from, "low");
    }));

    var totalLo = 0L;
    var totalHi = 0L;

    for (int n = 0; n < 1000; n++) {
      totalLo++;

      queue.add(new Signal("button", "roadcaster", "low"));
      while (!queue.isEmpty()) {
        var next = queue.remove();
        var fromModule = next.from;
        var targetModule = next.to;
        var pulse = next.pulse;

        if (!moduleState.containsKey(targetModule)) {
          continue;
        }

        var state = moduleState.get(targetModule);
        var type = moduleType.get(targetModule);
        var outCables = cables.get(targetModule);
        String toSend;

        if (type == 'b') {
          toSend = "low";
        } else if (type == '%') {
          var internalState = state.getOrDefault("internal", "low");
          if (pulse.equals("low")) {
            internalState = internalState.equals("low") ? "high" : "low";
            toSend = internalState;
          } else {
            toSend = null;
          }
          state.put("internal", internalState);
        } else if (type == '&') {
          state.put(fromModule, pulse);

          var allHigh = state.values().stream().allMatch(s -> s.equals("high"));
          toSend = allHigh ? "low" : "high";
        } else {
          throw new IllegalArgumentException();
        }

        if (toSend == null) {
          continue;
        }

        if (toSend.equals("low")) {
          totalLo += outCables.size();
        } else {
          totalHi += outCables.size();
        }

        outCables.forEach(output -> queue.add(new Signal(targetModule, output, toSend)));
      }
    }

    return totalHi * totalLo;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws Exception {
    var lines = reader.lines().toArray(String[]::new);

    var moduleState = new HashMap<String, Map<String, String>>();
    var moduleType = new HashMap<String, Integer>();
    var cables = new HashMap<String, List<String>>();
    var queue = new ArrayDeque<Signal>();

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];

      var split = line.substring(1).split(" -> ");
      var name = split[0];
      moduleState.put(name, new HashMap<>());

      var to = split[1].split(", ");
      cables.put(name, List.of(to));

      var type = line.codePointAt(0);
      moduleType.put(name, type);
    }

    cables.forEach((from, to) -> to.forEach(output -> {
      if (!moduleType.containsKey(output)) {
        return;
      }

      if (moduleType.get(output) == ((int) '%')) {
        return;
      }

      moduleState.get(output).put(from, "low");
    }));

    // assume that only 1 module sends pulses to rx
    var rxParent = cables.entrySet().stream().filter(pair -> pair.getValue().contains("rx")).map(Map.Entry::getKey).findAny().orElseThrow();
    var rxParentParent = cables.entrySet().stream().filter(pair -> pair.getValue().contains(rxParent)).map(Map.Entry::getKey).toList();

    var seen = new HashSet<String>();
    var rxCycle = 1L;

    for (int n = 0; n >= 0; n++) {
      queue.add(new Signal("button", "roadcaster", "low"));
      while (!queue.isEmpty()) {
        var next = queue.remove();
        var fromModule = next.from;
        var targetModule = next.to;
        var pulse = next.pulse;

        if (targetModule.equals(rxParent)) {
          // assume that rxParent is a conjunction (&) module
          for (var kv : moduleState.get(targetModule).entrySet()) {
            if (kv.getValue().equals("high") && seen.add(kv.getKey())) {
              rxCycle = MathUtil.lcm(rxCycle, n + 1);
            }

            if (seen.size() == rxParentParent.size()) {
              return rxCycle;
            }
          }
        }

        if (!moduleState.containsKey(targetModule)) {
          continue;
        }

        var state = moduleState.get(targetModule);
        var type = moduleType.get(targetModule);
        var outCables = cables.get(targetModule);
        String toSend;

        if (type == 'b') {
          toSend = "low";
        } else if (type == '%') {
          var internalState = state.getOrDefault("internal", "low");
          if (pulse.equals("low")) {
            internalState = internalState.equals("low") ? "high" : "low";
            toSend = internalState;
          } else {
            toSend = null;
          }
          state.put("internal", internalState);
        } else if (type == '&') {
          state.put(fromModule, pulse);

          var allHigh = state.values().stream().allMatch(s -> s.equals("high"));
          toSend = allHigh ? "low" : "high";
        } else {
          throw new IllegalArgumentException();
        }

        if (toSend == null) {
          continue;
        }

        outCables.forEach(output -> queue.add(new Signal(targetModule, output, toSend)));
      }
    }

    // should not happen
    return -1L;
  }

  private record Signal(String from, String to, String pulse) {}
}
