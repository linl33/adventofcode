package dev.linl33.adventofcode.lib.graph;

import dev.linl33.adventofcode.lib.function.BiIntConsumer;
import dev.linl33.adventofcode.lib.point.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

public final class GraphUtil {
  private static final Logger LOGGER = LogManager.getLogger(GraphUtil.class);

  public static <T> Optional<GraphPath<T>> aStar(T start, T end, Function<T, ? extends Collection<T>> neighborsFunc) {
    return aStar(start, end, neighborsFunc, (ToIntFunction<T>) GraphUtil::aStarNullHeuristic, GraphUtil::aStarNullCost);
  }

  public static <T> Optional<GraphPath<T>> aStar(T start,
                                                 T end,
                                                 Function<T, ? extends Collection<T>> neighborsFunc,
                                                 ToIntFunction<T> heuristic) {
    return aStar(start, end, neighborsFunc, heuristic, GraphUtil::aStarNullCost);
  }

  public static <T> Optional<GraphPath<T>> aStar(T start,
                                                 T end,
                                                 Function<T, ? extends Collection<T>> neighborsFunc,
                                                 Function<T, ToIntFunction<T>> heuristic) {
    return aStar(start, end, neighborsFunc, heuristic.apply(end), GraphUtil::aStarNullCost);
  }

  public static <T> Optional<GraphPath<T>> aStar(T start,
                                                 T end,
                                                 Function<T, ? extends Collection<T>> neighborsFunc,
                                                 Function<T, ToIntFunction<T>> heuristic,
                                                 ToIntBiFunction<T, T> cost) {
    return aStar(start, end, neighborsFunc, heuristic.apply(end), cost);
  }

  public static <T> Optional<GraphPath<T>> aStar(T start,
                                                 T end,
                                                 Function<T, ? extends Collection<T>> neighborsFunc,
                                                 ToIntFunction<T> heuristic,
                                                 ToIntBiFunction<T, T> cost) {
    var cameFrom = new HashMap<T, T>();
    var neighborCache = new HashMap<T, Collection<T>>();

    var gScore = new HashMap<>(Map.of(start, 0));
    var fScore = new HashMap<>(Map.of(start, gScore.get(start) + heuristic.applyAsInt(start)));

    var openSet = new PriorityQueue<T>(Comparator.comparing(fScore::get));
    openSet.add(start);

//    var visitCounter = 0;

    while (!openSet.isEmpty()) {
      var current = openSet.remove();
      if (current.equals(end)) {
//        LOGGER.debug("Visited {} nodes", visitCounter);

        var path = new HashMap<T, T>();

        var pathPointer = end;
        while (!pathPointer.equals(start)) {
          var pathNext = cameFrom.get(pathPointer);
          path.put(pathNext, pathPointer);
          pathPointer = pathNext;
        }

        return Optional.of(new GraphPath<>(path, start, end, gScore.get(end)));
      }

//      visitCounter++;
      var neighbors = neighborCache.computeIfAbsent(current, neighborsFunc);
      for (var neighbor : neighbors) {
        var tentativeGScore = gScore.get(current) + cost.applyAsInt(current, neighbor);
        if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
          cameFrom.put(neighbor, current);
          gScore.put(neighbor, tentativeGScore);
          fScore.put(neighbor, gScore.get(neighbor) + heuristic.applyAsInt(neighbor));

          openSet.add(neighbor);
        }
      }
    }

    return Optional.empty();
  }

  public static OptionalInt aStarLengthOnly(int start,
                                            int end,
                                            @NotNull IntFunction<int[]> neighborsFunc,
                                            @NotNull IntUnaryOperator heuristic,
                                            @NotNull IntBinaryOperator cost,
                                            int size) {
    return aStarIntInternal(
        start,
        end,
        neighborsFunc,
        heuristic,
        cost,
        size,
        BiIntConsumer.IDENTITY,
        gScore -> OptionalInt.of(gScore[end]),
        OptionalInt.empty()
    );
  }

  public static <T> Optional<GraphPath<T>> aStar(int start,
                                                 int end,
                                                 @NotNull IntFunction<int[]> neighborsFunc,
                                                 @NotNull IntUnaryOperator heuristic,
                                                 @NotNull IntBinaryOperator cost,
                                                 @NotNull IntFunction<T> idToNode,
                                                 int size) {
    var cameFrom = new int[size];

    return aStarIntInternal(
        start,
        end,
        neighborsFunc,
        heuristic,
        cost,
        size,
        (current, neighbor) -> cameFrom[neighbor] = current,
        gScore -> {
          var path = new HashMap<T, T>();

          var pathPointer = end;
          while (pathPointer != start) {
            var pathNext = cameFrom[pathPointer];
            path.put(idToNode.apply(pathNext), idToNode.apply(pathPointer));
            pathPointer = pathNext;
          }

          return Optional.of(new GraphPath<>(
              path,
              idToNode.apply(start),
              idToNode.apply(end),
              gScore[end]
          ));
        },
        Optional.empty()
    );
  }

  private static <R> R aStarIntInternal(int start,
                                       int end,
                                       @NotNull IntFunction<int[]> neighborsFunc,
                                       @NotNull IntUnaryOperator heuristic,
                                       @NotNull IntBinaryOperator cost,
                                       int size,
                                       @NotNull BiIntConsumer onNewEdge,
                                       @NotNull Function<int[], R> onPathFound,
                                       @NotNull R noPathValue) {
    var gScore = new int[size];
    Arrays.fill(gScore, Integer.MAX_VALUE);
    gScore[start] = 0;

    var openSetFScore = new int[size];
    Arrays.fill(openSetFScore, Integer.MAX_VALUE);
    openSetFScore[start] = heuristic.applyAsInt(start);

    var openSetCounter = 1;

//    var visitCounter = 0;

    var minFScore = openSetFScore[start];
    var minNode = start;
    var hasMin = true;

    while (openSetCounter > 0) {
      if (!hasMin) {
        minFScore = Integer.MAX_VALUE;

        for (int i = 0; i < openSetFScore.length; i++) {
          if (openSetFScore[i] < minFScore) {
            minFScore = openSetFScore[i];
            minNode = i;
          }
        }
      }

      openSetFScore[minNode] = Integer.MAX_VALUE;
      openSetCounter--;
      hasMin = false;

      if (minNode == end) {
        return onPathFound.apply(gScore);
      }

//      visitCounter++;

      var current = minNode;
      var currentGScore = gScore[current];
      var neighbors = neighborsFunc.apply(current);

      for (var neighbor : neighbors) {
        var neighborCost = cost.applyAsInt(current, neighbor);
        int tentativeGScore;
        if (neighborCost < gScore[neighbor] && (tentativeGScore = currentGScore + neighborCost) < gScore[neighbor]) {
          gScore[neighbor] = tentativeGScore;

          var neighborFScore = (openSetFScore[neighbor] = tentativeGScore + heuristic.applyAsInt(neighbor));
          if (openSetCounter == 0 || neighborFScore < minFScore) {
            minFScore = neighborFScore;
            minNode = neighbor;
            hasMin = true;
          }

          openSetCounter++;
          onNewEdge.accept(current, neighbor);
        }
      }
    }

    return noPathValue;
  }

  public static <T> Function<T, Optional<GraphPath<T>>> adaptAStar(T end,
                                                                   Function<T, ? extends Collection<T>> neighborsFunc,
                                                                   Function<T, ToIntFunction<T>> heuristic) {
    return start -> aStar(start, end, neighborsFunc, heuristic.apply(end));
  }

  private static <T> Integer aStarNullHeuristic(T point) {
    return 0;
  }

  public static <T extends Point<T>> ToIntFunction<T> manhattanDistHeuristic(T destination) {
    return destination::manhattanDistance;
  }

  private static <T> Integer aStarNullCost(T a, T b) {
    return 1;
  }
}
