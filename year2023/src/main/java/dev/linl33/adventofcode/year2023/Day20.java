package dev.linl33.adventofcode.year2023;

import dev.linl33.adventofcode.jmh.JmhBenchmarkOption;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class Day20 extends AdventSolution2023<Long, Long> {
  private static final int MAX_NEIGHBORS = 8;

  public static void main(String[] args) {
//    new Day20().runAndPrintAll();
//    new Day20().print(SolutionPart.PART_1, new ClasspathResourceIdentifier("day20test1"));
    new Day20().benchmark(JmhBenchmarkOption.PART_1, JmhBenchmarkOption.PERF_PROFILE);
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var cables = new int[(lines.length * 2 + 1) * MAX_NEIGHBORS];
    var flipFlopStates = -1L;
    var conjunctionStates = new long[lines.length];
    var moduleNameToId = new int[27 * 27];
    Arrays.fill(moduleNameToId, -1);
    var broadcastId = 0;

    for (int i = 0; i < lines.length; i++) {
      var line = lines[i];
      var type = line.codePointAt(0);

      int moduleId;

//      System.out.println(line);

      if (type == 'b') {
        moduleId = (i << 1) | 1;
        broadcastId = moduleId;
      } else {
//        if (type != '%' && type != '&') {
//          throw new IllegalStateException();
//        }

        // type % is odd, type & is even
        moduleId = (i << 1) | (type & 1);
        var moduleName = 27 * (line.codePointAt(1) & 0x1f) + (line.codePointAt(2) & 0x1f);
        moduleNameToId[moduleName] = moduleId;
      }

      var sepIdx = line.indexOf('-');
      var neighborCount = 0;
      var cableMapOffset = moduleId * MAX_NEIGHBORS;

      var j = sepIdx + 3;
      while (j < line.length()) {
        var nextModule = line.indexOf(',', j);
        if (nextModule == -1) {
          nextModule = line.length();
        }

        var secondDigit = (nextModule - j > 1) ? (line.codePointAt(j + 1) & 0x1f) : 0;
        var neighborName = 27 * (line.codePointAt(j) & 0x1f) + secondDigit;
        cables[cableMapOffset + 1 + neighborCount] = neighborName;
        neighborCount++;
        j = nextModule + 2;
      }

      cables[cableMapOffset] = neighborCount;
    }

    for (int i = 0; i < cables.length; i += MAX_NEIGHBORS) {
      var moduleNeighborCount = cables[i];
      for (int j = 0; j < moduleNeighborCount; j++) {
        var idx = i + j + 1;
        var moduleName = cables[idx];
        var moduleId = moduleNameToId[moduleName];
        cables[idx] = moduleId;
      }
    }

    for (int i = 0; i < cables.length; i += MAX_NEIGHBORS) {
      var moduleNeighborCount = cables[i];
      for (int j = 0; j < moduleNeighborCount; j++) {
        var neighborId = cables[i + j + 1];
        // neighbor is a conjunction, initialize memory slot as low (1)
        if ((neighborId & 1) == 0) {
          conjunctionStates[neighborId >> 1] |= 1L << ((i / MAX_NEIGHBORS) >> 1);
        }
      }
    }

    var queue = new long[lines.length * 8];

    var broadcastListeners = cables[broadcastId * MAX_NEIGHBORS];
    for (int i = 0; i < broadcastListeners; i++) {
      var neighborId = cables[broadcastId * MAX_NEIGHBORS + 1 + i];
      var signal = (1L << 16) | ((long) broadcastId << 8) | neighborId;
      queue[i] = signal;
    }

    var pulseCounts = new long[2];
    pulseCounts[1] = 1000L * (broadcastListeners + 1);

    for (int n = 0; n < 1000; n++) {
      var queueEnd = broadcastListeners;
      var queueFront = 0;

      while (queueFront < queueEnd) {
        var next = queue[queueFront++];
//        if (next == 0) {
//          throw new IllegalStateException();
//        }

        var target = (int) (next & 0xff);
        var pulse = next >> 16;

//        if ((target >> 1) >= lines.length || ((next & 0xff00) >> 9) >= lines.length) {
//          throw new IllegalStateException();
//        }

        var isFlipFlop = target & 1;
        long pulseOut;
        var targetMaskBit = target >> 1;
//        if (targetMaskBit >= lines.length) {
//          throw new IllegalStateException();
//        }

//        flipFlopStates ^= pulse << targetMaskBit;
//        pulseOut = (flipFlopStates >> targetMaskBit) & 1;
//
////        var source = (next >> 8) & 0xff;
//        var sourceMaskBit = (next & 0xff00) >> 9;
//
//        var conjunctionState = (conjunctionStates[targetMaskBit] & ~(1L << sourceMaskBit)) | (pulse << sourceMaskBit);
//        conjunctionStates[targetMaskBit] = conjunctionState;
////        conjunctionStates[targetMaskBit] |= (pulse << sourceMaskBit);

        if (isFlipFlop == 1) {
          flipFlopStates ^= pulse << targetMaskBit;
          pulseOut = (flipFlopStates >> targetMaskBit) & 1;
        } else {
          var sourceMaskBit = (next & 0xff00) >> 9;

          var conjunctionState = (conjunctionStates[targetMaskBit] & ~(1L << sourceMaskBit)) | (pulse << sourceMaskBit);
          conjunctionStates[targetMaskBit] = conjunctionState;
          // if conjunctionState == 0 then low (1) otherwise high (0)
          pulseOut = Long.numberOfTrailingZeros(conjunctionState) >> 6;
        }

//        if (isFlipFlop == 0) {
//          pulseOut = Long.numberOfTrailingZeros(conjunctionState) >> 6;
//        }

        var cableMapOffset = target * MAX_NEIGHBORS;
        // set neighborsCount to 0 if this is a flip-flop and the signal is high (0)
        var neighborsCount = cables[cableMapOffset] * ((target & pulse) | (~target & 1));

        var commonSignal = (pulseOut << 16) | (target << 8);
        for (int i = 0; i < neighborsCount; i++) {
          var neighborModuleId = cables[cableMapOffset + 1 + i];
          if (neighborModuleId == -1) {
            continue;
          }

          var signal = commonSignal | neighborModuleId;
//          if (cables[(neighborModuleId) * MAX_NEIGHBORS] == 0) {
//            throw new IllegalStateException();
//          }
          queue[queueEnd++] = signal;
        }

        pulseCounts[(int) pulseOut] += neighborsCount;
      }
    }

    return pulseCounts[0] * pulseCounts[1];

//    var moduleState = new HashMap<String, Map<String, String>>();
//    var moduleType = new HashMap<String, Integer>();
//    var cables = new HashMap<String, List<String>>();
//    var queue = new ArrayDeque<Signal>();
//
//    for (int i = 0; i < lines.length; i++) {
//      var line = lines[i];
//
//      var split = line.substring(1).split(" -> ");
//      var name = split[0];
//      moduleState.put(name, new HashMap<>());
//
//      var to = split[1].split(", ");
//      cables.put(name, List.of(to));
//
//      var type = line.codePointAt(0);
//      moduleType.put(name, type);
//    }
//
//    cables.forEach((from, to) -> to.forEach(output -> {
//      if (!moduleType.containsKey(output)) {
//        return;
//      }
//
//      if (moduleType.get(output) == ((int) '%')) {
//        return;
//      }
//
//      moduleState.get(output).put(from, "low");
//    }));
//
//    var totalLo = 0L;
//    var totalHi = 0L;
//
//    for (int n = 0; n < 1000; n++) {
//      totalLo++;
//
//      queue.add(new Signal("button", "roadcaster", "low"));
//      while (!queue.isEmpty()) {
//        var next = queue.remove();
//        var fromModule = next.from;
//        var targetModule = next.to;
//        var pulse = next.pulse;
//
//        if (!moduleState.containsKey(targetModule)) {
//          continue;
//        }
//
//        var state = moduleState.get(targetModule);
//        var type = moduleType.get(targetModule);
//        var outCables = cables.get(targetModule);
//        String toSend;
//
//        if (type == 'b') {
//          toSend = "low";
//        } else if (type == '%') {
//          var internalState = state.getOrDefault("internal", "low");
//          if (pulse.equals("low")) {
//            internalState = internalState.equals("low") ? "high" : "low";
//            toSend = internalState;
//          } else {
//            toSend = null;
//          }
//          state.put("internal", internalState);
//        } else if (type == '&') {
//          state.put(fromModule, pulse);
//
//          var allHigh = state.values().stream().allMatch(s -> s.equals("high"));
//          toSend = allHigh ? "low" : "high";
//        } else {
//          throw new IllegalArgumentException();
//        }
//
//        if (toSend == null) {
//          continue;
//        }
//
//        if (toSend.equals("low")) {
//          totalLo += outCables.size();
//        } else {
//          totalHi += outCables.size();
//        }
//
//        outCables.forEach(output -> queue.add(new Signal(targetModule, output, toSend)));
//      }
//    }
//
//    return totalHi * totalLo;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) {
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
