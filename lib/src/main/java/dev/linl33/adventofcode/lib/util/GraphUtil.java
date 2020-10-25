package dev.linl33.adventofcode.lib.util;

import dev.linl33.adventofcode.lib.point.Point;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

public class GraphUtil {
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
    var openSet = new HashSet<>(Collections.singleton(start));
    var cameFrom = new HashMap<T, T>();
    var neighborCache = new HashMap<T, Iterable<T>>();

    var gScore = new HashMap<>(Collections.singletonMap(start, 0));
    var fScore = new HashMap<>(Collections.singletonMap(start, gScore.get(start) + heuristic.applyAsInt(start)));

    var visitCounter = 0;

    while (!openSet.isEmpty()) {
      var current = AdventUtil.argMin(openSet, fScore::get);
      if (current.equals(end)) {
        // TODO: introduce debug logging
//        System.out.println("Visited: " + visitCounter);

        var path = new HashMap<T, T>();

        var pathPointer = end;
        while (!pathPointer.equals(start)) {
          var pathNext = cameFrom.get(pathPointer);
          path.put(pathPointer, pathNext);
          pathPointer = pathNext;
        }

        return path;
      }

      openSet.remove(current);
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

    throw new IllegalStateException();
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
