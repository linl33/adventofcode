package dev.linl33.adventofcode.lib;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class GraphNode<T> {
  private final String id;
  private final Set<GraphNode<T>> inNodes;
  private final Set<GraphNode<T>> outNodes;

  private T data;

  public String getId() {
    return id;
  }

  public Set<GraphNode<T>> getInNodes() {
    return inNodes;
  }

  public Set<GraphNode<T>> getOutNodes() {
    return outNodes;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public GraphNode(String id) {
    this(id, null);
  }

  public GraphNode(String id, T data) {
    this.id = id;
    this.inNodes = new TreeSet<>(Comparator.comparing(GraphNode::getId));
    this.outNodes = new TreeSet<>(Comparator.comparing(GraphNode::getId));
    this.data = data;
  }

  public int descendentCount() {
    return outNodes
        .stream()
        .filter(x -> this != x)
        .mapToInt(GraphNode::descendentCount)
        .sum() + outNodes.size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GraphNode<?> graphNode = (GraphNode<?>) o;

    return Objects.equals(id, graphNode.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "GraphNode{" +
        "id='" + id + '\'' +
        ", data='" + data + '\'' +
        '}';
  }
}
