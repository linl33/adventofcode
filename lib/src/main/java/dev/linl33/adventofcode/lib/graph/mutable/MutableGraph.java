package dev.linl33.adventofcode.lib.graph.mutable;

import dev.linl33.adventofcode.lib.graph.Graph;
import dev.linl33.adventofcode.lib.graph.GraphUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

public class MutableGraph implements Graph<String, MutableGraphNode> {
  private final Map<String, MutableGraphNode> nodes;
  private final Map<MutableGraphNode, Map<MutableGraphNode, Integer>> edges;

  @Override
  public MutableGraphNode[] getNodes() {
    return nodes.values().toArray(new MutableGraphNode[0]);
  }

  public MutableGraph() {
    this.nodes = new HashMap<>();
    this.edges = new HashMap<>();
  }

  @NotNull
  public MutableGraph addNode(@NotNull String nodeLabel) {
    nodes.putIfAbsent(nodeLabel, new MutableGraphNode(nodeLabel));
    return this;
  }

  @NotNull
  public MutableGraph removeNode(@NotNull String nodeLabel) {
    var removedNode = nodes.remove(nodeLabel);

    if (removedNode == null) {
      return this;
    }

    removedNode.inNodes().forEach(node -> node.outNodes().remove(removedNode));
    removedNode.outNodes().forEach(node -> node.inNodes().remove(removedNode));

    edges.remove(removedNode);
    edges.values().forEach(m -> m.keySet().remove(removedNode));

    return this;
  }

  @NotNull
  public MutableGraph addEdge(@NotNull String fromNode, @NotNull String toNode) {
    return addEdge(fromNode, toNode, 1);
  }

  @NotNull
  MutableGraph addEdge(@NotNull String fromNode, @NotNull String toNode, int cost) {
    var from = nodes.get(fromNode);
    var to = nodes.get(toNode);

    if (from == null || to == null) {
      throw new IllegalArgumentException("Cannot find fromNode or toNode");
    }

    from.outNodes().add(to);
    to.inNodes().add(from);

    edges.computeIfAbsent(from, __ -> new HashMap<>()).put(to, cost);

    return this;
  }

  @NotNull
  MutableGraph removeEdge(@NotNull String fromNode, @NotNull String toNode) {
    var from = nodes.get(fromNode);
    var to = nodes.get(toNode);

    if (from == null || to == null) {
      return this;
    }

    from.outNodes().remove(to);
    to.inNodes().remove(from);

    edges.get(from).remove(to);

    return this;
  }

  @Override
  public int cardinality() {
    return nodes.size();
  }

  @Override
  public @NotNull Optional<MutableGraphNode> getNode(@NotNull String nodeData) {
    return Optional.ofNullable(nodes.get(nodeData));
  }

  @Override
  public int getCost(@NotNull MutableGraphNode from, @NotNull MutableGraphNode to) {
    // throws NPE when the edge is not found
    return edges.getOrDefault(from, Map.of()).get(to);
  }

  @Override
  public @NotNull OptionalInt findPath(@NotNull MutableGraphNode from, @NotNull MutableGraphNode to) {
    return GraphUtil
        .aStar(from, to, MutableGraphNode::outNodes)
        .map(opt -> OptionalInt.of(opt.length()))
        .orElse(OptionalInt.empty());
  }
}
