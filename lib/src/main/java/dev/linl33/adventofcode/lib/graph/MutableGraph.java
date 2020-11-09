package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MutableGraph {
  private final Map<String, MutableGraphNode> nodes;

  public Map<String, MutableGraphNode> getNodes() {
    return nodes;
  }

  public MutableGraph() {
    this.nodes = new HashMap<>();
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

    return this;
  }

  @NotNull
  public MutableGraph addEdge(@NotNull String fromNode, @NotNull String toNode) {
    var from = nodes.get(fromNode);
    var to = nodes.get(toNode);

    if (from == null || to == null) {
      throw new IllegalArgumentException("Cannot find fromNode or toNode");
    }

    from.outNodes().add(to);
    to.inNodes().add(from);

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

    return this;
  }
}
