package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.OptionalInt;

public interface Graph<TData, TNode extends GraphNode<TNode>> {
  @NotNull
  TNode[] getNodes();

  int cardinality();

  default boolean isEmpty() {
    return cardinality() == 0;
  }

  @NotNull
  Optional<TNode> getNode(@NotNull TData nodeData);

  int getCost(@NotNull TNode from, @NotNull TNode to);

  @NotNull
  default OptionalInt findPath(@NotNull TNode from, @NotNull TNode to) {
    return GraphUtil
        .aStar(from, to, TNode::outNodes)
        .map(GraphPath::length)
        .map(OptionalInt::of)
        .orElse(OptionalInt.empty());
  }
}
