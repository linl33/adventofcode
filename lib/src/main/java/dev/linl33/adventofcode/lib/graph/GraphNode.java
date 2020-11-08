package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface GraphNode<T extends GraphNode<T>> {
  @NotNull
  Collection<T> inNodes();

  @NotNull
  Collection<T> outNodes();

  default int descendentCount() {
    return outNodes().size() + outNodes()
        .stream()
        .filter(x -> this != x)
        .mapToInt(GraphNode::descendentCount)
        .sum();
  }
}
