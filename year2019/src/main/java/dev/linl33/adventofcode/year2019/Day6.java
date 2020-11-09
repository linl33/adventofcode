package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.graph.intgraph.IntGraphBuilder;
import dev.linl33.adventofcode.lib.graph.intgraph.IntGraphNode;

import java.io.BufferedReader;
import java.util.Arrays;

public class Day6 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var graphBuilder = new IntGraphBuilder<String>();

    reader
        .lines()
        .map(line -> line.split("\\)"))
        .forEach(pair -> graphBuilder
            .addNode(pair[0])
            .addNode(pair[1])
            .addEdge(pair[0], pair[1], 1));

    return Arrays
        .stream(graphBuilder.build().getNodes())
        .mapToInt(IntGraphNode::descendentCount)
        .sum();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var graphBuilder = new IntGraphBuilder<String>();

    reader
        .lines()
        .map(line -> line.split("\\)"))
        .forEach(pair -> graphBuilder
            .addNode(pair[0])
            .addNode(pair[1])
            .addEdge(pair[0], pair[1], 1)
            .addEdge(pair[1], pair[0], 1)
        );

    var graph = graphBuilder
        .build();

    return graph
        .findPath(
            graph.getNode("YOU").orElseThrow(),
            graph.getNode("SAN").orElseThrow()
        )
        .orElseThrow() - 2;
  }
}
