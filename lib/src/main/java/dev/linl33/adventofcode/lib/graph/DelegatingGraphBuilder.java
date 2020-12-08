package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class DelegatingGraphBuilder<G, N, BN, B extends GraphBuilder<G, BN, ?>> implements GraphBuilder<G, N, DelegatingGraphBuilder<G, N, BN, B>> {
  private final B backingBuilder;
  private final Function<N, BN> mapper;

  public DelegatingGraphBuilder(@NotNull B backingBuilder, @NotNull Function<N, BN> mapper) {
    this.backingBuilder = backingBuilder;
    this.mapper = mapper;
  }

  @NotNull
  public DelegatingGraphBuilder<G, N, BN, B> addNode(@NotNull N node) {
    backingBuilder.addNode(mapper.apply(node));
    return this;
  }

  @NotNull
  public DelegatingGraphBuilder<G, N, BN, B> addEdge(@NotNull N from, @NotNull N to, int cost) {
    backingBuilder.addEdge(mapper.apply(from), mapper.apply(to), cost);
    return this;
  }

  @Override
  @NotNull
  public G build() {
    return backingBuilder.build();
  }
}
