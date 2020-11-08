package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

public class DataIntGraphNode<T> extends AbsIntGraphNode<DataIntGraphNode<T>> {
  private T data;

  @NotNull
  public T getData() {
    return data;
  }

  public void setData(@NotNull T data) {
    this.data = data;
  }

  protected DataIntGraphNode(int id, @NotNull T data) {
    super(id);

    this.data = data;
  }

  @Override
  public String toString() {
    return "DataIntGraphNode{" +
        "data=" + data +
        "} " + super.toString();
  }
}
