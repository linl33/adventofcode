package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day24 extends AdventSolution2024<Long, String> {
  public static void main() {
    new Day24().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws Exception {
    var groups = AdventUtil.readInputGrouped(reader).map(Stream::toList).toList();

    var initStates = groups.get(0);
    var gates = groups.get(1);

    var states = new HashMap<String, Boolean>();

    for (var s : initStates) {
      var parts = s.split(": ");
      states.put(parts[0], parts[1].equals("1"));
    }

    var gatesRemaining = gates.size();
    var visited = new HashSet<Integer>();

    while (gatesRemaining > 0) {
      for (var i = 0; i < gates.size(); i++) {
        if (visited.contains(i)) {
          continue;
        }

        var gate = gates.get(i);

        var parts = gate.split(" ");
        var left = parts[0];
        var right = parts[2];
        var op = parts[1];
        var dest = parts[4];

        if (states.containsKey(left) && states.containsKey(right)) {
          var leftVal = states.get(left);
          var rightVal = states.get(right);

          if (op.equals("AND")) {
            states.put(dest, leftVal & rightVal);
          } else if (op.equals("OR")) {
            states.put(dest, leftVal | rightVal);
          } else if (op.equals("XOR")) {
            states.put(dest, leftVal ^ rightVal);
          } else {
            throw new IllegalStateException("Unknown op: " + op);
          }

          gatesRemaining--;
          visited.add(i);
        }
      }
    }

    var zStates = states.keySet().stream().filter(k -> k.startsWith("z")).sorted().toList().reversed();
    var bStr = zStates.stream().map(states::get).map(b -> b ? "1" : "0").reduce(String::concat).orElseThrow();

    return Long.parseLong(bStr, 2);
  }

  @Override
  public String part2(@NotNull BufferedReader reader) throws Exception {
    var groups = AdventUtil.readInputGrouped(reader).map(Stream::toList).toList();

    var gates = groups.get(1);
    var outputsToSwap = new TreeSet<String>();

    for (var i = 0; i < gates.size(); i++) {
      var gate = gates.get(i);

      var parts = gate.split(" ");
      var left = parts[0];
      var right = parts[2];
      var op = parts[1];
      var dest = parts[4];

      if (op.equals("OR")) {
        var xorCount = 0;
        var andCount = 0;

        for (var j = 0; j < gates.size(); j++) {
          var gate2 = gates.get(j);

          var parts2 = gate2.split(" ");
          var left2 = parts2[0];
          var right2 = parts2[2];
          var op2 = parts2[1];
          var dest2 = parts2[4];

          if (left2.equals(dest) || right2.equals(dest)) {
            if (op2.equals("XOR")) {
              xorCount++;
            } else if (op2.equals("AND")) {
              andCount++;
            }
          }
        }

        if ((xorCount != 1 || andCount != 1) && !dest.equals("z45")) {
          System.out.println("OR rule XOR " + xorCount + " AND " + andCount + ": " + gate);
          outputsToSwap.add(dest);
        }
      }

      if (op.equals("AND")) {
        var orCount = 0;

        for (var j = 0; j < gates.size(); j++) {
          var gate2 = gates.get(j);

          var parts2 = gate2.split(" ");
          var left2 = parts2[0];
          var right2 = parts2[2];
          var op2 = parts2[1];
          var dest2 = parts2[4];

          if (left2.equals(dest) || right2.equals(dest)) {
            if (op2.equals("OR")) {
              orCount++;
            }
          }
        }

        if (orCount != 1 && (!left.equals("x00") && !right.equals("x00"))) {
          System.out.println("AND rule OR " + orCount + ": " + gate);
          outputsToSwap.add(dest);
        }
      }

      if (op.equals("XOR")) {
        var xorCount = 0;
        var andCount = 0;
        var orCount = 0;

        for (var j = 0; j < gates.size(); j++) {
          var gate2 = gates.get(j);

          var parts2 = gate2.split(" ");
          var left2 = parts2[0];
          var right2 = parts2[2];
          var op2 = parts2[1];
          var dest2 = parts2[4];

          if (left2.equals(dest) || right2.equals(dest)) {
            if (op2.equals("XOR")) {
              xorCount++;
            } else if (op2.equals("AND")) {
              andCount++;
            } else if (op2.equals("OR")) {
              orCount++;
            }
          }
        }

        if (orCount != 0) {
          System.out.println("XOR rule3 OR " + orCount + ": " + gate);
          outputsToSwap.add(dest);
        } else {
          if (left.startsWith("x") || right.startsWith("x")) {
            if ((xorCount != 1 || andCount != 1) && !dest.equals("z00")) {
              System.out.println("XOR rule XOR " + xorCount + " AND " + andCount + ": " + gate);
              outputsToSwap.add(dest);
            }
          } else {
            if (xorCount != 0 || andCount != 0) {
              System.out.println("XOR rule2 XOR " + xorCount + " AND " + andCount + ": " + gate);
              outputsToSwap.add(dest);
            }
          }
        }
      }
    }

    return String.join(",", outputsToSwap);
  }
}
