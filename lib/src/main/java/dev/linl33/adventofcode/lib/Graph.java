package dev.linl33.adventofcode.lib;

import java.util.Map;
import java.util.TreeMap;

public class Graph<T> {
  private final Map<String, GraphNode<T>> nodes;

  public Map<String, GraphNode<T>> getNodes() {
    return nodes;
  }

  public Graph() {
    this.nodes = new TreeMap<>();
  }

  public GraphNode<T> addNode(String id) {
    return addNode(id, null);
  }

  public GraphNode<T> addNode(GraphNode<T> node) {
    return addNode(node.getId(), node.getData());
  }

  public GraphNode<T> addNode(String id, T data) {
    nodes.putIfAbsent(id, new GraphNode<>(id, data));
    return nodes.get(id);
  }

  public Graph<T> removeNode(String id) {
    var toRemove = nodes.get(id);

    for (var toNode : toRemove.getOutNodes()) {
      toNode.getInNodes().remove(toRemove);
    }

    for (var fromNode : toRemove.getInNodes()) {
      fromNode.getOutNodes().remove(toRemove);
    }

    nodes.remove(id);
    return this;
  }

  public Graph<T> addEdge(String from, String to) {
    nodes.get(from).getOutNodes().add(nodes.get(to));
    nodes.get(to).getInNodes().add(nodes.get(from));

    return this;
  }

  public Graph<T> removeEdge(String from, String to) {
    nodes.get(from).getOutNodes().remove(nodes.get(to));
    nodes.get(to).getInNodes().remove(nodes.get(from));

    return this;
  }

  @Override
  public String toString() {
    return "Graph{" +
        "nodes=" + nodes +
        '}';
  }
}
