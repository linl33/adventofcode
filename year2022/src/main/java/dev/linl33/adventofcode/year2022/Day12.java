package dev.linl33.adventofcode.year2022;

import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import jdk.incubator.vector.ShortVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class Day12 extends AdventSolution2022<Integer, Integer> {
  private static final VectorSpecies<Short> SPECIES = ShortVector.SPECIES_PREFERRED;

  private static final int POTENTIAL_ENDPOINTS_MAX = 100;

  public static void main(String[] args) {
    new Day12().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    return calculatePathLength(reader, false);
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    return calculatePathLength(reader, true);
  }

  private static int calculatePathLength(@NotNull BufferedReader reader, boolean fromEnd) {
    var grid = new RowArrayGrid(reader);
    var gridBackingArray = grid.array();

    var start = 0;
    var end = 0;
    var startFound = false;
    var endFound = false;
    for (int i = 0; i < gridBackingArray.length; i++) {
      if (gridBackingArray[i] == 'E') {
        gridBackingArray[i] = 'z';
        end = i + 1;

        if ((endFound = true) && startFound) {
          break;
        }
      } else if (gridBackingArray[i] == 'S') {
        gridBackingArray[i] = 'a';
        start = i + 1;

        if ((startFound = true) && endFound) {
          break;
        }
      }
    }

    var pathStart = fromEnd ? end : start;
    var pathEnd = fromEnd ? 0 : end;

    IntUnaryOperator heuristic;
    var heuristicCache = new int[aStarGraphSize(grid.width(), grid.height())];
    if (!fromEnd) {
      var pathEndX = pathEnd % grid.width();
      var pathEndY = pathEnd / grid.width();

      heuristic = p -> {
        if (heuristicCache[p] != 0) {
          return heuristicCache[p];
        }

        p = p - 1;
        var x = p % grid.width();
        var y = p / grid.width();

        return (heuristicCache[p + 1] = Math.abs(pathEndX - x) + Math.abs(pathEndY - y));
      };
    } else {
      var potentialEndpoints = new short[POTENTIAL_ENDPOINTS_MAX * 2];
      var potentialEndpointsCount = 0;

      gridLoop:
      for (int i = 0; i < gridBackingArray.length; i++) {
        if (gridBackingArray[i] != 'a') {
          continue;
        }

        var x = i % grid.width();
        var y = i / grid.width();

        for (int deltaY = -1; deltaY <= 1; deltaY++) {
          for (int deltaX = -1; deltaX <= 1; deltaX++) {
            if ((deltaY == 0) == (deltaX == 0)) {
              continue;
            }

            var x2 = x + deltaX;
            var y2 = y + deltaY;

            if (!grid.isWithinBounds(x2, y2)) {
              continue;
            }

            // the endpoint has to have a neighbor with elevation 'b'
            // if its lowest elevation is 'c' then it's inaccessible
            // if its highest elevation is 'a' then it has a neighbor with a shorter path
            if (grid.get(x2, y2) == 'b') {
              potentialEndpoints[potentialEndpointsCount] = (short) x;
              potentialEndpoints[potentialEndpointsCount + POTENTIAL_ENDPOINTS_MAX] = (short) y;
              potentialEndpointsCount++;
              continue gridLoop;
            }
          }
        }
      }

      var alignedLength = Math.ceilDiv(potentialEndpointsCount, SPECIES.length()) * SPECIES.length();
      if (potentialEndpointsCount % SPECIES.length() != 0) {
        Arrays.fill(potentialEndpoints, potentialEndpointsCount, alignedLength, (short) (grid.width() * 2));
        Arrays.fill(potentialEndpoints, potentialEndpointsCount + POTENTIAL_ENDPOINTS_MAX, alignedLength + POTENTIAL_ENDPOINTS_MAX, (short) (grid.height() * 2));
      }

      var finalPotentialEndpointsCount = potentialEndpointsCount;
      heuristic = p -> {
        if (heuristicCache[p] != 0) {
          return heuristicCache[p];
        }

        p = p - 1;
        var x = p % grid.width();
        var y = p / grid.width();

        var xVector = (ShortVector) SPECIES.broadcast(x);
        var yVector = (ShortVector) SPECIES.broadcast(y);
        var minVector = (ShortVector) SPECIES.broadcast(Short.MAX_VALUE);

        for (int i = 0; i < alignedLength; i += SPECIES.length()) {
          var pathX = (ShortVector) SPECIES.fromArray(potentialEndpoints, i);
          var pathY = (ShortVector) SPECIES.fromArray(potentialEndpoints, i + finalPotentialEndpointsCount);

          pathX = xVector.sub(pathX).abs();
          pathY = yVector.sub(pathY).abs();
          minVector = minVector.min(pathX.add(pathY));
        }

        var min = minVector.reduceLanes(VectorOperators.MIN);
        return (heuristicCache[p + 1] = min);
      };
    }

    var elevationDiff = (fromEnd ? (IntBinaryOperator) ((a, b) -> a - b) : (IntBinaryOperator) ((a, b) -> b - a));

    var neighbors = new int[4];
    var finalStart = start;
    var pathLength = GraphUtil.aStarLengthOnly(
        pathStart,
        pathEnd,
        p -> {
          p = p - 1;
          var x = p % grid.width();
          var y = p / grid.width();
          var currVal = gridBackingArray[p];

          Arrays.fill(neighbors, finalStart);
          var neighborPointer = 0;

          for (int deltaY = -1; deltaY <= 1; deltaY++) {
            for (int deltaX = -1; deltaX <= 1; deltaX++) {
              if ((deltaY == 0) == (deltaX == 0)) {
                // skip diagonal moves
                continue;
              }

              var x2 = x + deltaX;
              var y2 = y + deltaY;

              if (!grid.isWithinBounds(x2, y2)) {
                continue;
              }

              var p2 = y2 * grid.width() + x2;
              var neighborVal = gridBackingArray[p2];

              if (elevationDiff.applyAsInt(currVal, neighborVal) < 2) {
                neighbors[neighborPointer++] = (fromEnd && neighborVal == 'a') ? pathEnd : (p2 + 1);
              }
            }
          }

          return neighbors;
        },
        heuristic,
        (a, b) -> b == finalStart ? Integer.MAX_VALUE : 1,
        aStarGraphSize(grid.width(), grid.height())
    );

    return pathLength.orElseThrow();
  }

  private static int aStarGraphSize(int width, int height) {
    return width * height + 1;
  }
}
