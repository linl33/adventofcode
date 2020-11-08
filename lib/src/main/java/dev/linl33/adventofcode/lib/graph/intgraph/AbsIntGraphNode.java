package dev.linl33.adventofcode.lib.graph.intgraph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;

public abstract class AbsIntGraphNode<T extends AbsIntGraphNode<T>> implements IntGraphNode<T> {
  private final int id;
  private final Collection<T> inNodes;
  private final Collection<T> outNodes;

  private Collection<T> inNodesUnmodifiable;
  private Collection<T> outNodesUnmodifiable;

  private boolean updateInNodeUnmodifiable;
  private boolean updateOutNodeUnmodifiable;

  private T lastInNode;
  private T lastOutNode;

  @Override
  public int id() {
    return id;
  }

  @Override
  public @NotNull @UnmodifiableView Collection<T> inNodes() {
    if (updateInNodeUnmodifiable) {
      inNodesUnmodifiable = Collections.unmodifiableCollection(inNodes);
    }

    return inNodesUnmodifiable;
  }

  @Override
  public @NotNull @UnmodifiableView Collection<T> outNodes() {
    if (updateOutNodeUnmodifiable) {
      outNodesUnmodifiable = Collections.unmodifiableCollection(outNodes);
    }

    return outNodesUnmodifiable;
  }

  @Override
  public @NotNull Optional<T> lastInNode() {
    return Optional.ofNullable(lastInNode);
  }

  @Override
  public @NotNull Optional<T> lastOutNode() {
    return Optional.ofNullable(lastOutNode);
  }

  protected AbsIntGraphNode(int id) {
    this.id = id;
    this.inNodes = new ArrayList<>();
    this.outNodes = new ArrayList<>();
    this.inNodesUnmodifiable = List.of();
    this.outNodesUnmodifiable = List.of();
    this.updateInNodeUnmodifiable = false;
    this.updateOutNodeUnmodifiable = false;
    this.lastInNode = null;
    this.lastOutNode = null;
  }

  @Override
  public void addInNode(@NotNull T other) {
    inNodes.add(other);
    updateInNodeUnmodifiable = true;
    lastInNode = other;
  }

  @Override
  public void addOutNode(@NotNull T other) {
    outNodes.add(other);
    updateOutNodeUnmodifiable = true;
    lastOutNode = other;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbsIntGraphNode<?> that = (AbsIntGraphNode<?>) o;

    return id == that.id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public String toString() {
    return "AbsIntGraphNode{" +
        "id=" + id +
        '}';
  }
}
