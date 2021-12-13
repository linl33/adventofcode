package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.graph.mutable.MutableGraph;
import dev.linl33.adventofcode.lib.graph.mutable.MutableGraphBuilder;
import dev.linl33.adventofcode.lib.graph.mutable.MutableGraphNode;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Day12 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day12().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    MutableGraph g = buildGraph(reader);
    return countPaths(g, (node, path) -> path.stream().anyMatch(n -> n.getLabel().equals(node.getLabel())));
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    var graph = buildGraph(reader);

    return countPaths(graph, (node, path) -> {
      var freq = path
          .stream()
          .filter(n -> Character.isLowerCase(n.getLabel().charAt(0)))
          .collect(Collectors.groupingBy(MutableGraphNode::getLabel, Collectors.counting()));

      var visitedTwice = freq.values().stream().anyMatch(i -> i > 1);
      if (!visitedTwice) {
        return false;
      } else {
        return path.stream().anyMatch(n -> n.getLabel().equals(node.getLabel()));
      }
    });
  }

  @NotNull
  private MutableGraph buildGraph(@NotNull BufferedReader reader) {
    var lines = reader.lines().toList();

    var graphBuilder = new MutableGraphBuilder();

    for (var s : lines) {
      var line = s.split("-");
      var left = line[0];
      var right = line[1];

      graphBuilder.addNode(left);
      graphBuilder.addNode(right);
      graphBuilder.addEdge(left, right, 1);
      graphBuilder.addEdge(right, left, 1);
    }

    return graphBuilder.build();
  }

  private static int countPaths(@NotNull MutableGraph graph,
                                @NotNull BiPredicate<MutableGraphNode, List<MutableGraphNode>> shouldRemove) {
    var start = graph.getNode("start").orElseThrow();
    var end = graph.getNode("end").orElseThrow();
    var pathCounter = 0;

    var queue = new ArrayDeque<ArrayList<MutableGraphNode>>();
    queue.add(new ArrayList<>());
    queue.getFirst().add(start);
    while (!queue.isEmpty()) {
      var nextPath = queue.pop();
      var next = nextPath.get(nextPath.size() - 1);

      for (var neighbor : next.outNodes()) {
        if ((!neighbor.equals(end) && !Character.isUpperCase(neighbor.getLabel().charAt(0))) && (neighbor.equals(start) || shouldRemove.test(neighbor, nextPath))) {
          continue;
        }

        if (neighbor.equals(end)) {
          pathCounter++;
        } else {
          var pathCopy = new ArrayList<>(nextPath);
          pathCopy.add(neighbor);
          queue.push(pathCopy);
        }
      }
    }

    return pathCounter;
  }
}
