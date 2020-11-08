package dev.linl33.adventofcode.lib.graph.intgraph;

import dev.linl33.adventofcode.lib.graph.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface IntGraphNode<T extends IntGraphNode<T>> extends GraphNode<T>, Comparable<T> {
  int id();
  @NotNull
  Optional<T> lastInNode();

  @NotNull
  Optional<T> lastOutNode();

  void addInNode(@NotNull T other);

  void addOutNode(@NotNull T other);

  @Override
  default int compareTo(@NotNull T o) {
    return Integer.compare(id(), o.id());
  }
}
