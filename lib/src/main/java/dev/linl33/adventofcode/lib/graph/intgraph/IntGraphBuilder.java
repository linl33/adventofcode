package dev.linl33.adventofcode.lib.graph.intgraph;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class IntGraphBuilder<T> {
  private final Collection<T> nodes;
  private final Map<T, Map<T, Integer>> edges;
  private Supplier<IdLayout<T>> layoutSupplier;
  private IdGenerator<T> idGenerator;
  private BiFunction<DataIntGraphNode<T>, DataIntGraphNode<T>, OptionalInt> costFunction;

  public IntGraphBuilder() {
    this.nodes = new ArrayList<>();
    this.edges = new HashMap<>();
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
  public IntGraphBuilder<T> addNode(@NotNull T data) {
    // TODO: support SimpleIntGraphNode

    nodes.add(data);
    return this;
  }

  @Contract(value = "_, _, _ -> this", mutates = "this")
  public IntGraphBuilder<T> addEdge(@NotNull T from, @NotNull T to, int cost) {
    if (cost < 0) {
      throw new IllegalArgumentException("Negative edge cost unsupported");
    }

    edges.computeIfAbsent(from, __ -> new HashMap<>()).put(to, cost);
    return this;
  }

  @NotNull
  public IntGraph<T, DataIntGraphNode<T>> build() {
    var layout = Objects.requireNonNullElse(layoutSupplier, this::defaultIdLayoutBuilder).get();

    idGenerator = Objects.requireNonNullElseGet(idGenerator, () -> IdGenerator.asIdGenerator(layout));
    costFunction = Objects.requireNonNullElse(costFunction, this::defaultCostFunction);

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

  @NotNull
  private OptionalInt defaultCostFunction(DataIntGraphNode<T> from, DataIntGraphNode<T> to) {
    return Optional
        .ofNullable(edges.getOrDefault(from.getData(), Map.of()).get(to.getData()))
        .map(OptionalInt::of)
        .orElseGet(OptionalInt::empty);
  }
}
