package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface IntGraphNode<T extends IntGraphNode<T>> extends Comparable<T> {
  int id();

  @NotNull
  Collection<T> inNodes();

  @NotNull
  Collection<T> outNodes();

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
