package dev.linl33.adventofcode.lib.graph.intgraph;

import dev.linl33.adventofcode.lib.graph.Graph;
import dev.linl33.adventofcode.lib.graph.GraphPath;
import dev.linl33.adventofcode.lib.graph.GraphUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.*;
import java.util.stream.Collectors;

public class IntGraph<TData, TNode extends IntGraphNode<TNode>> implements Graph<TData, TNode> {

  private final TNode[] nodes;
  private final IdLayout<TData> idLayout;
  private final IdGenerator<TData> idGenerator;
  private int[][] adjacencyMatrix;
  private int[][] adjacencyList;

  @Override
  public TNode[] getNodes() {
    return nodes;
  }

  @NotNull
  public IdLayout<TData> getIdLayout() {
    return idLayout;
  }

  @NotNull
  public IdGenerator<TData> getIdGenerator() {
    return idGenerator;
  }

  public int[][] getAdjacencyMatrix() {
    return adjacencyMatrix;
  }

  public int[][] getAdjacencyList() {
    return adjacencyList;
  }

  protected IntGraph(@NotNull TNode[] nodes,
                     @NotNull BiFunction<TNode, TNode, OptionalInt> costFunction,
                     @NotNull IdLayout<TData> idLayout,
                     @NotNull IdGenerator<TData> idGenerator) {
    this.nodes = nodes;
    this.idLayout = idLayout;
    this.idGenerator = idGenerator;

    populateEdges(costFunction);
  }

  @NotNull
  public OptionalInt findPath(@NotNull TNode start,
                              @NotNull TNode end) {
    return findPath(start, end, UnaryOperator.identity());
  }

  @NotNull
  public OptionalInt findPath(@NotNull TNode start,
                              @NotNull TNode end,
                              @NotNull UnaryOperator<IntFunction<int[]>> neighborFunc) {

    return GraphUtil.aStarLengthOnly(
        start.id(),
        end.id(),
        neighborFunc.apply(this::defaultNeighborFunction),
        __ -> 0,
        this::defaultCostFunction,
        cardinality()
    );
  }

  @NotNull
  public Optional<GraphPath<TNode>> findPathFull(@NotNull TNode start,
                                                 @NotNull TNode end) {
    return GraphUtil.aStar(
        start.id(),
        end.id(),
        this::defaultNeighborFunction,
        __ -> 0,
        this::defaultCostFunction,
        i -> getNodes()[i],
        cardinality()
    );
  }

  @NotNull
  public Optional<TNode> findNode(@NotNull Predicate<TNode> predicate) {
    return Arrays
        .stream(getNodes())
        .filter(predicate)
        .findAny();
  }

  @NotNull
  public <K> Map<K, TNode> findNodeMulti(@NotNull Map<K, Predicate<TNode>> predicates) {
    return Arrays
        .stream(getNodes())
        .filter(predicates
            .values()
            .stream()
            .reduce(__ -> false, Predicate::or)
        )
        .map(node -> predicates
            .entrySet()
            .stream()
            .filter(kv -> kv.getValue().test(node))
            .findAny()
            .map(kv -> Map.entry(kv.getKey(), node))
            .orElseThrow()
        )
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue)
        );
  }

  @NotNull
  public Optional<TNode> getNode(int nodeId) {
    return nodeId >= 0 && nodeId < getNodes().length ?
        Optional.ofNullable(getNodes()[nodeId]) :
        Optional.empty();
  }

  @NotNull
  @Override
  public Optional<TNode> getNode(@NotNull TData nodeData) {
    return getNode(getIdGenerator().generateId(nodeData));
  }

  @Override
  public int getCost(@NotNull TNode from, @NotNull TNode to) {
    return adjacencyMatrix[from.id()][to.id()];
  }

  @Override
  public int cardinality() {
    return nodes.length;
  }

  private void populateEdges(@NotNull BiFunction<TNode, TNode, OptionalInt> costFunction) {
    var arrSize = getIdLayout().allocationSize();

    var matrix = new int[arrSize][arrSize];
    var list = new int[arrSize][];

    var allNodes = getNodes();
    for (TNode node : allNodes) {
      if (node == null) {
        continue;
      }

      var edges = new int[cardinality()];
      var edgeCounter = 0;

      for (TNode n2 : allNodes) {
        if (n2 == null || node == n2) {
          continue;
        }

        var pathCost = costFunction.apply(node, n2);
        if (pathCost.isPresent()) {
          matrix[node.id()][n2.id()] = pathCost.getAsInt();
          edges[edgeCounter++] = n2.id();

          node.addOutNode(n2);
          n2.addInNode(node);
        }
      }

      list[node.id()] = Arrays.copyOf(edges, edgeCounter);
    }

    this.adjacencyMatrix = matrix;
    this.adjacencyList = list;
  }

  private int[] defaultNeighborFunction(int id) {
    return adjacencyList[id];
  }

  private int defaultCostFunction(int idStart, int idEnd) {
    return adjacencyMatrix[idStart][idEnd];
  }
}
