package dev.linl33.adventofcode.lib.graph.intgraph;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class IntGraphBuilder<T> {
  private final Collection<T> nodes;
  private Supplier<IdLayout<T>> layoutSupplier;
  private IdGenerator<T> idGenerator;
  private BiFunction<DataIntGraphNode<T>, DataIntGraphNode<T>, OptionalInt> costFunction;

  public IntGraphBuilder() {
    this.nodes = new ArrayList<>();
    this.layoutSupplier = null;
    this.idGenerator = null;
    this.costFunction = null;
  }

  @Contract(value = "_ -> this", mutates = "this")
  public IntGraphBuilder<T> withIdLayout(@NotNull IdLayout<T> layout) {
    this.layoutSupplier = () -> layout;
    return this;
  }

  @Contract(value = "_ -> this", mutates = "this")
  public IntGraphBuilder<T> withIdLayoutBuilder(@NotNull IdLayoutBuilder<T> layoutBuilder) {
    this.layoutSupplier = () -> layoutBuilder.build(nodes);
    return this;
  }

  @Contract(mutates = "this")
  public IntGraphBuilder<T> withDefaultIdGenerator() {
    this.idGenerator = null;
    return this;
  }

  @Contract(value = "_ -> this", mutates = "this")
  public IntGraphBuilder<T> withCostFunction(@NotNull BiFunction<DataIntGraphNode<T>, DataIntGraphNode<T>, OptionalInt> costFunction) {
    this.costFunction = costFunction;
    return this;
  }

  @Contract(value = "_ -> this", mutates = "this")
  public IntGraphBuilder<T> withEdges(@NotNull Map<T, Map<T, Integer>> edges) {
    // TODO: make sure withEdges/withCostFunction are not both called
    // TODO: allow adding edges with builder pattern

    this.costFunction = (n1, n2) -> Optional
        .ofNullable(edges.getOrDefault(n1.getData(), Map.of()).get(n2.getData()))
        .map(OptionalInt::of)
        .orElseGet(OptionalInt::empty);

    return this;
  }

  @Contract(value = "_ -> this", mutates = "this")
  public IntGraphBuilder<T> addNode(@NotNull T data) {
    // TODO: support SimpleIntGraphNode

    nodes.add(data);
    return this;
  }

  @NotNull
  public IntGraph<T, DataIntGraphNode<T>> build() {
    layoutSupplier = Objects.<Supplier<IdLayout<T>>>requireNonNullElseGet(layoutSupplier, () -> this::defaultIdLayoutBuilder);
    var layout = layoutSupplier.get();

    idGenerator = Objects.requireNonNullElseGet(idGenerator, () -> IdGenerator.asIdGenerator(layout));
    costFunction = Objects.requireNonNullElseGet(costFunction, () -> (a, b) -> OptionalInt.of(1));

    @SuppressWarnings("unchecked")
    DataIntGraphNode<T>[] nodeArr = new DataIntGraphNode[layout.allocationSize()];

    for (T nodeData : nodes) {
      var id = idGenerator.generateId(nodeData);
      nodeArr[id] = new DataIntGraphNode<>(id, nodeData);
    }

    return new IntGraph<>(nodeArr, costFunction, layout, idGenerator);
  }

  @NotNull
  private IdLayout<T> defaultIdLayoutBuilder() {
    return new IdLayoutBuilder<T>().addField(Function.identity()).build(nodes);
  }
}
