package dev.linl33.adventofcode.year2024;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;

public class Day23 extends AdventSolution2024<Integer, String> {
  public static void main() {
    new Day23().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var edges = new HashMap<String, Set<String>>();

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var parts = line.split("-");
      edges.putIfAbsent(parts[0], HashSet.newHashSet(lines.length));
      edges.putIfAbsent(parts[1], HashSet.newHashSet(lines.length));

      edges.get(parts[0]).add(parts[1]);
      edges.get(parts[1]).add(parts[0]);
    }

    var triangles = 0;
    for (var kv : edges.entrySet()) {
      var neighbors = kv.getValue().toArray(String[]::new);

      for (int i = 0; i < neighbors.length; i++) {
        var left = neighbors[i];

        for (var j = i + 1; j < neighbors.length; j++) {
          var right = neighbors[j];
          if (edges.get(left).contains(right)) {
            if (kv.getKey().charAt(0) == 't' || left.charAt(0) == 't' || right.charAt(0) == 't') {
              triangles++;
            }
          }
        }
      }
    }

    return triangles / 3;
  }

  @Override
  public String part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);

    var edges = new LinkedHashMap<Short, List<Short>>();
    var degreeMax = 0;

    for (var i = 0; i < lines.length; i++) {
      var line = lines[i];

      var left = (short) ((line.charAt(0) - 'a') * 32 + (line.charAt(1) - 'a'));
      var right = (short) ((line.charAt(3) - 'a') * 32 + (line.charAt(4) - 'a'));

      edges.computeIfAbsent(left, _ -> new ArrayList<>(lines.length / 2));
      edges.computeIfAbsent(right, _ -> new ArrayList<>(lines.length / 2));

      edges.get(left).add(right);
      edges.get(right).add(left);

      degreeMax = Math.max(degreeMax, edges.get(left).size());
    }

    var cliqueSizeLimit = degreeMax + 1;

    var maximumClique = new short[cliqueSizeLimit];
    var maximumCliqueSize = 0;

    var maximalClique = new short[cliqueSizeLimit];

    for (var kv : edges.entrySet()) {
      short start = kv.getKey();

      maximalClique[0] = start;
      var cliqueSize = 1;

      for (var neighbor : kv.getValue()) {
        var add = true;

        var neighborEdges = edges.get(neighbor);
        for (var i = 0; i < cliqueSize; i++) {
          if (!neighborEdges.contains(maximalClique[i])) {
            add = false;
            break;
          }
        }

        if (add) {
          maximalClique[cliqueSize++] = neighbor;
        }
      }

      if (cliqueSize > maximumCliqueSize) {
        var tmp = maximumClique;
        maximumClique = maximalClique;
        maximalClique = tmp;

        maximumCliqueSize = cliqueSize;
      }
    }

    Arrays.sort(maximumClique, 0, maximumCliqueSize);
    var sb = new StringBuilder(maximumCliqueSize * 3);

    for (var i = 0; i < maximumCliqueSize; i++) {
      sb.append((char) ((maximumClique[i] / 32) + 'a'));
      sb.append((char) ((maximumClique[i] % 32) + 'a'));
      sb.append(',');
    }

    return sb.substring(0, sb.length() - 1);
  }
}
