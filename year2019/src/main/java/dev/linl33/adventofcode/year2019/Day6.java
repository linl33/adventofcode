package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.graph.intgraph.IntGraphBuilder;
import dev.linl33.adventofcode.lib.graph.intgraph.IntGraphNode;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day6 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var graphBuilder = new IntGraphBuilder<String>();
    var edges = new HashMap<String, Map<String, Integer>>();

    reader
        .lines()
        .map(line -> line.split("\\)"))
        .forEach(pair -> {
          graphBuilder
              .addNode(pair[0])
              .addNode(pair[1]);

          edges.computeIfAbsent(pair[0], __ -> new HashMap<>()).put(pair[1], 1);
        });

    var graph = graphBuilder
        .withEdges(edges)
        .build();

    return Arrays.stream(graph.getNodes())
        .mapToInt(IntGraphNode::descendentCount)
        .sum();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var graphBuilder = new IntGraphBuilder<String>();
    var edges = new HashMap<String, Map<String, Integer>>();

    reader
        .lines()
        .map(line -> line.split("\\)"))
        .forEach(pair -> {
          graphBuilder
              .addNode(pair[0])
              .addNode(pair[1]);

          edges.computeIfAbsent(pair[0], __ -> new HashMap<>()).put(pair[1], 1);
          edges.computeIfAbsent(pair[1], __ -> new HashMap<>()).put(pair[0], 1);
        });

    var graph = graphBuilder
        .withEdges(edges)
        .build();

    return graph
        .findPath(
            graph.getNode("YOU").orElseThrow(),
            graph.getNode("SAN").orElseThrow()
        )
        .orElseThrow() - 2;
  }
}
