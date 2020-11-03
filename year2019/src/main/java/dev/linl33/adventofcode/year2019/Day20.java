package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.Graph;
import dev.linl33.adventofcode.lib.GraphNode;
import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.GridVisitResult;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import dev.linl33.adventofcode.lib.util.GraphUtil;
import dev.linl33.adventofcode.lib.util.GridUtil;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day20 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
    new Day20().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    GridUtil.fillEnclaves(grid);

    var ptToId = findPortals(grid);

    var portalArr = ptToId.keySet().toArray(Point2D[]::new);
    var portalCount = portalArr.length;
    var ptIdx = IntStream
        .range(0, portalCount)
        .collect(
            () -> new TreeMap<Point2D, Integer>(Comparator.comparing(ptToId::get)),
            (map, idx) -> map.put(portalArr[idx], idx),
            Map::putAll
        );

    var adjacencyMatrix = new int[portalCount][portalCount];
    var adjacencyList = new int[portalCount][];

    for (Point2D portal : ptIdx.keySet()) {
      Function<Point2D, List<Point2D>> neighborFunc = (Point2D gridPt) -> GridUtil
          .orthogonalNeighbors(grid, gridPt)
          .stream()
          .filter(neighbor -> neighbor.equals(portal) || grid.configuration().isEmptySpace(grid.get(neighbor)))
          .collect(Collectors.toList());

      var portalId = ptToId.get(portal);
      var portalName = portalId.substring(0, 2);
      var portalIdx = ptIdx.get(portal);

      var edgesArr = new int[portalCount - 1];
      var edgeCounter = 0;

      for (Point2D portal2 : ptIdx.keySet()) {
        if (portal == portal2) {
          continue;
        }

        var portal2Id = ptToId.get(portal2);
        var portal2Name = portal2Id.substring(0, 2);
        var portal2Idx = ptIdx.get(portal2);

        if (portal2Name.equals(portalName)) {
          edgesArr[edgeCounter++] = portal2Idx;
          adjacencyMatrix[portalIdx][portal2Idx] = 1;
        } else {
          var path = GraphUtil.aStar(portal2, portal, neighborFunc);

          if (path != null) {
            edgesArr[edgeCounter++] = portal2Idx;
            adjacencyMatrix[portalIdx][portal2Idx] = path.length() - 2;
          }
        }
      }

      adjacencyList[portalIdx] = Arrays.copyOf(edgesArr, edgeCounter);
    }

    var pathLength = GraphUtil.aStarLengthOnly(
        ptIdx.firstEntry().getValue(),
        ptIdx.lastEntry().getValue(),
        i -> adjacencyList[i],
        __ -> 0,
        (a, b) -> adjacencyMatrix[a][b],
        ptIdx.size()
    );

    if (pathLength < 0) {
      throw new IllegalArgumentException();
    }

    return pathLength;
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    GridUtil.fillEnclaves(grid);

    grid.print();

    var ptToId = findPortals(grid);

    var portalArr = ptToId.keySet().toArray(Point2D[]::new);
    var portalCount = portalArr.length;
    var ptIdx = IntStream
        .range(0, portalCount)
        .collect(
            () -> new TreeMap<Point2D, Integer>(Comparator.comparing(ptToId::get)),
            (map, idx) -> map.put(portalArr[idx], idx),
            Map::putAll
        );

    var adjacencyMatrix = new int[portalCount][portalCount];
    var adjacencyList = new int[portalCount][];

    for (Point2D portal : ptIdx.keySet()) {
      Function<Point2D, List<Point2D>> neighborFunc = (Point2D gridPt) -> GridUtil
          .orthogonalNeighbors(grid, gridPt)
          .stream()
          .filter(neighbor -> neighbor.equals(portal) || grid.configuration().isEmptySpace(grid.get(neighbor)))
          .collect(Collectors.toList());

      var portalId = ptToId.get(portal);
      var portalName = portalId.substring(0, 2);
      var portalIdx = ptIdx.get(portal);

      var edgesArr = new int[portalCount - 1];
      var edgeCounter = 0;

      for (Point2D portal2 : ptIdx.keySet()) {
        if (portal == portal2) {
          continue;
        }

        var portal2Id = ptToId.get(portal2);
        var portal2Name = portal2Id.substring(0, 2);
        var portal2Idx = ptIdx.get(portal2);

        if (portal2Name.equals(portalName)) {
          edgesArr[edgeCounter++] = portal2Idx;
          adjacencyMatrix[portalIdx][portal2Idx] = 1;
        } else {
          var path = GraphUtil.aStar(portal2, portal, neighborFunc);

          if (path != null) {
            edgesArr[edgeCounter++] = portal2Idx;
            adjacencyMatrix[portalIdx][portal2Idx] = path.length() - 2;
          }
        }
      }

      adjacencyList[portalIdx] = Arrays.copyOf(edgesArr, edgeCounter);
    }

    var idToPt = AdventUtil.invertMap(ptToId);
    var idxToId = ptIdx
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getValue,
            kv -> ptToId.get(kv.getKey())
        ));

    var graph = new Graph<String>();
    for (Map.Entry<String, Point2D> portal : idToPt.entrySet()) {
      if (portal.getKey().length() == 2) {
        graph.addNode(portal.getKey());

        for (int neighborIdx : adjacencyList[ptIdx.get(idToPt.get(portal.getKey()))]) {
          var id = idxToId.get(neighborIdx);

          if (id.length() != 2) {
            id += "A";
          }

          graph.addNode(id);
          graph.addEdge(portal.getKey(), id);
        }
      } else {
        graph.addNode(portal.getKey() + "A");
        graph.addNode(portal.getKey() + "B");

        var receivingNodeId = portal.getKey().substring(0, 3) + (portal.getKey().endsWith("E") ? "I" : "E") + "B";
        graph.addNode(receivingNodeId);
        graph.addEdge(portal.getKey() + "A", receivingNodeId);

        for (int neighborIdx : adjacencyList[ptIdx.get(idToPt.get(portal.getKey()))]) {
          var id = idxToId.get(neighborIdx);

          // skip the matching portal
          if (id.substring(0, 2).equals(portal.getKey().substring(0, 2))) {
            continue;
          }

          if (id.length() != 2) {
            id += "A";
          }

          graph.addNode(id);
          graph.addEdge(portal.getKey() + "B", id);
        }
      }
    }

    var path = GraphUtil.aStar(
        "AA",
        "ZZ",
        portal -> {
          int level;
          if (portal.length() == 2 || portal.length() == 5) {
            level = 0;
          } else {
            level = Integer.parseInt(portal.substring(5));
            portal = portal.substring(0, 5);
          }

          if (level > 50) {
            return List.of();
          }

          var neighborStream = graph.getNodes().get(portal).getOutNodes().stream().map(GraphNode::getId);

          if (level == 0) {
            neighborStream = neighborStream.filter(s -> s.length() == 2 || s.charAt(3) != 'E');
          } else {
            neighborStream = neighborStream.filter(s -> s.length() != 2);
          }

          if (level == 0) {
            if (portal.length() == 2 || !portal.endsWith("A")) {
              neighborStream = neighborStream.map(s -> s.length() == 2 ? s : s + "1");
            }
          } else {
            if (portal.endsWith("A")) {
              neighborStream = neighborStream.map(s -> s.substring(0, 5) + level);
            } else {
              neighborStream = neighborStream.map(s -> s.substring(0, 5) + (s.charAt(3) == 'E' ? level - 1 : level + 1));
            }
          }

          return neighborStream.collect(Collectors.toList());
        },
        (ToIntFunction<String>) __ -> 0,
        (left, right) -> {
          var leftIdx = ptIdx.get(idToPt.get(left.length() == 2 ? left : left.substring(0, 4)));
          var rightIdx = ptIdx.get(idToPt.get(right.length() == 2 ? right : right.substring(0, 4)));

          return adjacencyMatrix[leftIdx][rightIdx];
        }
    );

    return Objects.requireNonNull(path).length();
  }

  private static HashMap<Point2D, String> findPortals(Grid grid) {
    var portals = new HashMap<Point2D, String>();

    grid.visit((x, y, value) -> {
      if (value < 'A' || value > 'Z') {
        return GridVisitResult.CONTINUE;
      }

      var currPt = new Point2D(x, y);

      Stream
          .of(GridUtil.move(currPt, GridUtil.HEADING_SOUTH), GridUtil.move(currPt, GridUtil.HEADING_EAST))
          .filter(grid::isWithinBounds)
          .map(grid::get)
          .filter(i -> i >= 'A' && i <= 'Z')
          .findAny()
          .ifPresent(i -> {
            var iChar = (char) i.intValue();
            var valChar = (char) value;

            var neighbors = GridUtil.orthogonalNeighbors(grid, currPt);
            var portalPos = neighbors.size() == 1 ? neighbors.get(0) : currPt;

            var name = iChar < valChar ? (String.valueOf(iChar) + valChar) : (String.valueOf(valChar) + iChar);
            portals.put(portalPos, name);
          });

      return GridVisitResult.CONTINUE;
    });

    return portals
        .entrySet()
        .stream()
        .collect(Collectors.collectingAndThen(
            Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())),
            groupedBy -> {
              portals.replaceAll((pt, name) -> resolveName(grid, name, pt, groupedBy.get(name)));
              return portals;
            }
        ));
  }

  private static String resolveName(Grid grid, String baseName, Point2D point, List<Point2D> points) {
    if (points.size() == 1) {
      return baseName;
    }

    var otherPoint = points.get(0);
    if (otherPoint.equals(point)) {
      otherPoint = points.get(1);
    }

    var midPoint = new Point2D(grid.width() / 2, grid.height() / 2);
    var pointRadius = midPoint.manhattanDistance(point);
    var otherPointRadius = midPoint.manhattanDistance(otherPoint);

    if (pointRadius == otherPointRadius) {
      pointRadius = midPoint.squaredEuclideanDistance(point);
      otherPointRadius = midPoint.squaredEuclideanDistance(otherPoint);

      if (pointRadius == otherPointRadius) {
        throw new IllegalStateException();
      }
    }

    return baseName + "_" + (pointRadius < otherPointRadius ? "I" : "E");
  }
}
