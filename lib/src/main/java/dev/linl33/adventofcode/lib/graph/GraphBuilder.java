package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

public interface GraphBuilder<G, N, T extends GraphBuilder<G, N, T>> {
  @NotNull
  T addNode(@NotNull N node);

  @NotNull
  T addEdge(@NotNull N from, @NotNull N to, int cost);

  @NotNull
  G build();
}
