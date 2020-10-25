package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.Graph;
import dev.linl33.adventofcode.lib.GraphNode;

import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

public class Day6 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var graph = new Graph<String>();

    reader
        .lines()
        .map(line -> line.split("\\)"))
        .forEach(pair -> {
          graph.addNode(pair[0]);
          graph.addNode(pair[1]);

          graph.addEdge(pair[1], pair[0]);
        });

    return graph
        .getNodes()
        .values()
        .stream()
        .mapToInt(GraphNode::descendentCount)
        .sum();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var graph = new Graph<String>();

    reader
        .lines()
        .map(line -> line.split("\\)"))
        .forEach(pair -> {
          graph.addNode(pair[0]);
          graph.addNode(pair[1]);

          graph.addEdge(pair[0], pair[1]);
        });

    return findPathToSan(graph.getNodes().get("YOU"), new HashSet<>()) - 2;
  }

  private static int findPathToSan(GraphNode<String> curr, Set<String> visited) {
    if (visited.contains(curr.getId())) {
      return -1;
    }

    var count = 0;
    for (var outNode : curr.getOutNodes()) {
      if (outNode.getId().equals("SAN")) {
        return 1;
      }

      visited.add(curr.getId());
      var outDist = findPathToSan(outNode, visited);

      if (outDist > 0) {
        count = outDist + 1;
      }
    }

    for (var inNode : curr.getInNodes()) {
      visited.add(curr.getId());
      var inDist = findPathToSan(inNode, visited);

      if (inDist > 0) {
        count = inDist + 1;
      }
    }

    return count;
  }
}
