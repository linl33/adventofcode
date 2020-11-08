package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class MutableGraphNode implements GraphNode<MutableGraphNode> {
  private final Collection<MutableGraphNode> inNodes;
  private final Collection<MutableGraphNode> outNodes;

  private String label;

  @Override
  public @NotNull Collection<MutableGraphNode> inNodes() {
    return inNodes;
  }

  @Override
  public @NotNull Collection<MutableGraphNode> outNodes() {
    return outNodes;
  }

  @Nullable
  public String getLabel() {
    return label;
  }

  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  public MutableGraphNode(@Nullable String label) {
    this.inNodes = new ArrayList<>();
    this.outNodes = new ArrayList<>();
    this.label = label;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MutableGraphNode that = (MutableGraphNode) o;

    return label != null ? label.equals(that.label) : that.label == null;
  }

  @Override
  public int hashCode() {
    return label != null ? label.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "MutableGraphNode{" +
        "label='" + label + '\'' +
        '}';
  }
}
