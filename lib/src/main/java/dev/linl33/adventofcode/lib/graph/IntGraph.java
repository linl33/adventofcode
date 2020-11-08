package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IntGraph<TData, TNode extends IntGraphNode<TNode>> {
  public static class Builder<T> {
    private final Collection<T> nodes;
    private List<Map<Object, Integer>> intAssignment;
    private List<Function<T, ?>> fieldAccessors;
    private BiFunction<DataIntGraphNode<T>, DataIntGraphNode<T>, OptionalInt> costFunction;

    public Builder() {
      this.nodes = new ArrayList<>();
      this.fieldAccessors = null;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public Builder<T> withAccessors(@NotNull List<Function<T, ?>> fieldAccessors) {
      this.fieldAccessors = fieldAccessors;
      return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public Builder<T> withDefaultAccessors(@NotNull Class<T> tClass) {
      this.fieldAccessors = buildAccessors(tClass);
      return this;
    }

    public Builder<T> withDefaultIntAssignment() {
      if (fieldAccessors == null) {
        // TODO:
        throw new IllegalStateException();
      }

      this.intAssignment = assignIntToField();
      return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public Builder<T> withCostFunction(@NotNull BiFunction<DataIntGraphNode<T>, DataIntGraphNode<T>, OptionalInt> costFunction) {
      this.costFunction = costFunction;
      return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public Builder<T> withEdges(@NotNull Map<T, Map<T, Integer>> edges) {
      // TODO: make sure withEdges/withCostFunction are not both called
      // TODO: allow adding edges with builder pattern

      this.costFunction = (n1, n2) -> Optional
          .ofNullable(edges.getOrDefault(n1.getData(), Map.of()).get(n2.getData()))
          .map(OptionalInt::of)
          .orElseGet(OptionalInt::empty);
      return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public Builder<T> addNode(@NotNull T data) {
      // TODO: support SimpleIntGraphNode

      nodes.add(data);
      return this;
    }

    @NotNull
    public IntGraph<T, DataIntGraphNode<T>> build() {
      if (fieldAccessors == null || costFunction == null || intAssignment == null) {
        // TODO: eliminate this
        throw new IllegalStateException();
      }

      var sizes = findSizes(intAssignment);

      var layout = IntStream
          .range(0, sizes.length)
          .mapToObj(i -> new IdLayout.LayoutElement<>(sizes[i], intAssignment.get(i).size(), fieldAccessors.get(i)))
          .collect(Collectors.collectingAndThen(Collectors.toList(), IdLayout::new));

      ToIntFunction<T> idGenerator = (T nodeData) -> {
        int id = 0;

        for (int i = 0; i < layout.elements().size(); i++) {
          var element = layout.elements().get(i);

          var fieldIntValue = intAssignment
              .get(i)
              .get(element.accessor().apply(nodeData));

          if (i == 0) {
            id = fieldIntValue;
          } else {
            id = (id << element.length()) | fieldIntValue;
          }
        }

        return id;
      };

      @SuppressWarnings("unchecked")
      DataIntGraphNode<T>[] nodeArr = new DataIntGraphNode[layout.allocationSize()];

      for (T nodeData : nodes) {
        var id = idGenerator.applyAsInt(nodeData);
        nodeArr[id] = new DataIntGraphNode<>(id, nodeData);
      }

      return new IntGraph<>(nodeArr, costFunction, layout, idGenerator);
    }

    @NotNull
    private List<Map<Object, Integer>> assignIntToField() {
      var fieldIdList = new ArrayList<Map<Object, Integer>>();

      for (var field : fieldAccessors) {
        var fieldIMap = new HashMap<Object, Integer>();
        fieldIdList.add(fieldIMap);

        for (T nodeData : nodes) {
          var fieldValue = field.apply(nodeData);
          fieldIMap.computeIfAbsent(fieldValue, __ -> fieldIMap.size());
        }
      }

      return fieldIdList;
    }

    private static int[] findSizes(@NotNull List<Map<Object, Integer>> fieldIdList) {
      return fieldIdList
          .stream()
          .mapToInt(map -> Integer.SIZE - Integer.numberOfLeadingZeros(map.size() - 1))
          .toArray();
    }

    @NotNull
    private static <T> List<Function<T, ?>> buildAccessors(@NotNull Class<T> clazz) {
      if (!clazz.isRecord()) {
        return List.of(Function.identity());
      }

      var accessors = new ArrayList<Function<T, ?>>();
      for (var comp : clazz.getRecordComponents()) {
        accessors.add((T t) -> {
          try {
            return comp.getAccessor().invoke(t);
          } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        });
      }

      return accessors;
    }
  }

  private final TNode[] nodes;
  private final IdLayout<TData> idLayout;
  private final ToIntFunction<TData> idGenerator;
  private int[][] adjacencyMatrix;
  private int[][] adjacencyList;

  public TNode[] getNodes() {
    return nodes;
  }

  @NotNull
  public IdLayout<TData> getIdLayout() {
    return idLayout;
  }

  @NotNull
  public ToIntFunction<TData> getIdGenerator() {
    return idGenerator;
  }

  public int[][] getAdjacencyMatrix() {
    return adjacencyMatrix;
  }

  public int[][] getAdjacencyList() {
    return adjacencyList;
  }

  private IntGraph(@NotNull TNode[] nodes,
                   @NotNull BiFunction<TNode, TNode, OptionalInt> costFunction,
                   @NotNull IdLayout<TData> idLayout,
                   @NotNull ToIntFunction<TData> idGenerator) {
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
        getIdLayout().allocationSize()
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
  public Optional<TNode> getNode(@NotNull TData nodeData) {
    return getNode(getIdGenerator().applyAsInt(nodeData));
  }

  public int getCost(@NotNull TNode from, @NotNull TNode to) {
    return adjacencyMatrix[from.id()][to.id()];
  }

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
