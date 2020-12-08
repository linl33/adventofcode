package dev.linl33.adventofcode.lib.graph.intgraph;

import dev.linl33.adventofcode.lib.graph.GraphBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class IntGraphBuilder<T> implements GraphBuilder<IntGraph<T, DataIntGraphNode<T>>, T, IntGraphBuilder<T>> {
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
  @Override
  public @NotNull IntGraphBuilder<T> addNode(@NotNull T data) {
    // TODO: support SimpleIntGraphNode

    nodes.add(data);
    return this;
  }

  @Contract(value = "_, _, _ -> this", mutates = "this")
  @Override
  public @NotNull IntGraphBuilder<T> addEdge(@NotNull T from, @NotNull T to, int cost) {
    if (cost < 0) {
      throw new IllegalArgumentException("Negative edge cost unsupported");
    }

    edges.computeIfAbsent(from, __ -> new HashMap<>()).put(to, cost);
    return this;
  }

  @NotNull
  @Override
  public IntGraph<T, DataIntGraphNode<T>> build() {
    var layout = Objects.requireNonNullElse(layoutSupplier, this::defaultIdLayoutBuilder).get();

    idGenerator = Objects.requireNonNullElseGet(idGenerator, () -> IdGenerator.asIdGenerator(layout));
    costFunction = Objects.requireNonNullElse(costFunction, defaultCostFunction());

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
  private BiFunction<DataIntGraphNode<T>, DataIntGraphNode<T>, OptionalInt> defaultCostFunction() {
    var idEdges = new HashMap<Integer, Map<Integer, Integer>>(edges.size());

    // convert the edges from a map of [obj -> obj -> cost] into a map of [id -> id -> cost]
    edges.forEach((edge, children) ->
        children.forEach((node, cost) ->
            idEdges
                .computeIfAbsent(idGenerator.generateId(edge), __ -> new HashMap<>(children.size()))
                .put(idGenerator.generateId(node), cost)
        )
    );

    return (from, to) -> Optional
        .ofNullable(idEdges
            .getOrDefault(from.id(), Map.of())
            .get(to.id())
        )
        .map(OptionalInt::of)
        .orElseGet(OptionalInt::empty);
  }
}
