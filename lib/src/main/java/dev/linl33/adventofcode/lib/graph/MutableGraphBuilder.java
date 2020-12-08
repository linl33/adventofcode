package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

public class MutableGraphBuilder implements GraphBuilder<MutableGraph, String, MutableGraphBuilder> {
  private final MutableGraph graph;

  public MutableGraphBuilder() {
    graph = new MutableGraph();
  }

  @Override
  public @NotNull MutableGraphBuilder addNode(@NotNull String node) {
    graph.addNode(node);

    return this;
  }

  @Override
  public @NotNull MutableGraphBuilder addEdge(@NotNull String from, @NotNull String to, int cost) {
    graph.addEdge(from, to, cost);

    return this;
  }

  @Override
  public @NotNull MutableGraph build() {
    return graph;
  }
}
