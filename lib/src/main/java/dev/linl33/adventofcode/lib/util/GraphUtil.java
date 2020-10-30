package dev.linl33.adventofcode.lib.util;

import dev.linl33.adventofcode.lib.GraphPath;
import dev.linl33.adventofcode.lib.point.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.*;

final public class GraphUtil {
  private static final Logger LOGGER = LogManager.getLogger(GraphUtil.class);

  public static <T> Map<T, T> aStar(T start, T end, Function<T, ? extends Iterable<T>> neighborsFunc) {
    return aStar(start, end, neighborsFunc, GraphUtil::aStarNullHeuristic, GraphUtil::aStarNullCost);
  }

  public static <T> Map<T, T> aStar(T start,
                                    T end,
                                    Function<T, ? extends Iterable<T>> neighborsFunc,
                                    ToIntFunction<T> heuristic) {
    return aStar(start, end, neighborsFunc, heuristic, GraphUtil::aStarNullCost);
  }

  public static <T> Map<T, T> aStar(T start,
                                    T end,
                                    Function<T, ? extends Iterable<T>> neighborsFunc,
                                    ToIntFunction<T> heuristic,
                                    ToIntBiFunction<T, T> cost) {
    var cameFrom = new HashMap<T, T>();
    var neighborCache = new HashMap<T, Iterable<T>>();

    var gScore = new HashMap<>(Collections.singletonMap(start, 0));
    var fScore = new HashMap<>(Collections.singletonMap(start, gScore.get(start) + heuristic.applyAsInt(start)));

    var openSet = new PriorityQueue<T>(Comparator.comparing(fScore::get));
    openSet.add(start);

    var visitCounter = 0;

    while (!openSet.isEmpty()) {
//      var current = AdventUtil.argMin(openSet, fScore::get);
      var current = openSet.remove();
      if (current.equals(end)) {
//        LOGGER.debug("Visited {} nodes", visitCounter);

        var path = new HashMap<T, T>();

        var pathPointer = end;
        while (!pathPointer.equals(start)) {
          var pathNext = cameFrom.get(pathPointer);
          path.put(pathPointer, pathNext);
          pathPointer = pathNext;
        }

        return path;
      }

      visitCounter++;
      for (var neighbor : neighborCache.computeIfAbsent(current, neighborsFunc)) {
        var tentativeGScore = gScore.get(current) + cost.applyAsInt(current, neighbor);
        if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
          cameFrom.put(neighbor, current);
          gScore.put(neighbor, tentativeGScore);
          fScore.put(neighbor, gScore.get(neighbor) + heuristic.applyAsInt(neighbor));

          openSet.add(neighbor);
        }
      }
    }

    return null;
  }

  public static <T> GraphPath<T> aStarAsGraphPath(T start,
                                               T end,
                                               Function<T, ? extends Iterable<T>> neighborsFunc,
                                               ToIntFunction<T> heuristic,
                                               ToIntBiFunction<T, T> cost) {
    var cameFrom = new HashMap<T, T>(100);
//    var neighborCache = new HashMap<T, Iterable<T>>(500);

    var gScore = new HashMap<T, Integer>(100);
    gScore.put(start, 0);
    var fScore = new HashMap<T, Integer>(100);
    fScore.put(start, gScore.get(start) + heuristic.applyAsInt(start));

    var openSet = new PriorityQueue<T>(Comparator.comparing(fScore::get));
//    var openSet = new ArrayList<T>(100);
    openSet.add(start);

    var visitCounter = 0;

    while (!openSet.isEmpty()) {
//      var current = AdventUtil.argMin(openSet, fScore::get);
      var current = openSet.remove();
      if (current.equals(end)) {
//        LOGGER.debug("Visited {} nodes", visitCounter);

        var path = new HashMap<T, T>(cameFrom.size());
        var length = 0;

        var pathPointer = end;
        while (!pathPointer.equals(start)) {
          var pathNext = cameFrom.get(pathPointer);

          path.put(pathPointer, pathNext);
          length += cost.applyAsInt(pathPointer, pathNext);

          pathPointer = pathNext;
        }

        return new GraphPath<>(path, length);
      }

      visitCounter++;
      for (var neighbor : /*neighborCache.computeIfAbsent(current, neighborsFunc)*/ neighborsFunc.apply(current)) {
        var tentativeGScore = gScore.get(current) + cost.applyAsInt(current, neighbor);
        if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
          cameFrom.put(neighbor, current);
          gScore.put(neighbor, tentativeGScore);
          fScore.put(neighbor, gScore.get(neighbor) + heuristic.applyAsInt(neighbor));

          openSet.add(neighbor);
        }
      }
    }

    return null;
  }

  public static int aStarLengthOnly(int start,
                                    int end,
                                    IntFunction<int[]> neighborsFunc,
                                    IntUnaryOperator heuristic,
                                    IntBinaryOperator cost,
                                    int size) {
    var gScore = new int[size];
    Arrays.fill(gScore, Integer.MAX_VALUE);
    gScore[start] = 0;

    var openSetFScore = new int[size];
    Arrays.fill(openSetFScore, Integer.MAX_VALUE);
    openSetFScore[start] = heuristic.applyAsInt(start);

    var openSetCounter = 1;

    var visitCounter = 0;

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
        return gScore[end];
      }

      visitCounter++;

      var current = minNode;
      var neighbors = neighborsFunc.apply(current);

      for (int neighbor : neighbors) {
        var tentativeGScore = gScore[current] + cost.applyAsInt(current, neighbor);
        if (tentativeGScore < gScore[neighbor]) {
          gScore[neighbor] = tentativeGScore;

          openSetFScore[neighbor] = tentativeGScore + heuristic.applyAsInt(neighbor);
          openSetCounter++;

          if (openSetFScore[neighbor] < minFScore || openSetCounter == 1) {
            minFScore = openSetFScore[neighbor];
            minNode = neighbor;
            hasMin = true;
          }
        }
      }
    }

    return Integer.MIN_VALUE;
  }

  public static <T> Function<T, Map<T, T>> adaptAStar(T end,
                                                      Function<T, ? extends Iterable<T>> neighborsFunc,
                                                      ToIntFunction<T> heuristic) {
    return start -> aStar(start, end, neighborsFunc, heuristic);
  }

  private static <T> Integer aStarNullHeuristic(T point) {
    return 0;
  }

  public static <T extends Point<T>> ToIntFunction<T> manhattanDistHeuristic(T destination) {
    return curr -> GeomUtil.manhattanDist(curr, destination);
  }

  private static <T> Integer aStarNullCost(T a, T b) {
    return 1;
  }
}
