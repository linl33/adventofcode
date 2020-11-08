package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.graph.GraphPath;
import dev.linl33.adventofcode.lib.graph.AbsIntGraphNode;
import dev.linl33.adventofcode.lib.graph.DataIntGraphNode;
import dev.linl33.adventofcode.lib.graph.IntGraph;
import dev.linl33.adventofcode.lib.grid.Grid;
import dev.linl33.adventofcode.lib.grid.GridVisitResult;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.util.GridUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day20 extends AdventSolution2019<Integer, Integer> {
  public static final String ENTRANCE_EXIT_NAME = "AA";
  private static final Portal P_ENTRANCE = new Portal(null, ENTRANCE_EXIT_NAME, Portal.Edge.INTERIOR, Portal.Direction.EXIT, 0);
  private static final Portal P_EXIT = new Portal(null, ENTRANCE_EXIT_NAME, Portal.Edge.EXTERIOR, Portal.Direction.ENTER, 0);

  public static void main(String[] args) {
    new Day20().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var grid = new RowArrayGrid(reader);
    GridUtil.fillEnclaves(grid);

    var ptToId = findPortals(grid);

    var graphBuilder = new IntGraph.Builder<Portal>();

    for (Map.Entry<Point2D, String> entry : ptToId.entrySet()) {
      Portal r;
      var portalStr = entry.getValue();
      if (portalStr.length() == 2) {
        r = new Portal(portalStr.equals("AA") ? P_ENTRANCE : P_EXIT, entry.getKey());
      } else {
        r = new Portal(entry.getKey(), portalStr, Portal.Direction.EXIT);
      }

      graphBuilder.addNode(r);
    }

    var graph = graphBuilder
        .withAccessors(List.of(Portal::name, Portal::edge))
        .withCostFunction((n1, n2) -> {
          if (n1.getData().name().equals(n2.getData().name()) && !n1.getData().name().equals(ENTRANCE_EXIT_NAME)) {
            return OptionalInt.of(1);
          }

          return findPathOnGrid(grid, n1.getData().position(), n2.getData().position());
        })
        .withDefaultIntAssignment()
        .build();

    var entrance = graph.getNode(P_ENTRANCE).orElseThrow();
    var exit = graph.getNode(P_EXIT).orElseThrow();

    return graph
        .findPath(entrance, exit)
        .orElseThrow();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    var grid = new RowArrayGrid(reader);
    GridUtil.fillEnclaves(grid);

    var ptToId = findPortals(grid);

    var graphBuilder = new IntGraph.Builder<Portal>();

    for (Map.Entry<Point2D, String> entry : ptToId.entrySet()) {
      var portalStr = entry.getValue();

      if (portalStr.length() == 2) {
        graphBuilder
            .addNode(new Portal(portalStr.equals("AA") ? P_ENTRANCE : P_EXIT, entry.getKey()));
      } else {
        graphBuilder
            .addNode(new Portal(entry.getKey(), portalStr, Portal.Direction.EXIT))
            .addNode(new Portal(entry.getKey(), portalStr, Portal.Direction.ENTER));
      }
    }

    var graph = graphBuilder
        .withAccessors(List.of(Portal::name, Portal::edge, Portal::direction))
        .withCostFunction((n1, n2) -> {
          var portal1 = n1.getData();
          var portal2 = n2.getData();

          // this is the exit, it has no out edge
          if (portal1.edge() == Portal.Edge.EXTERIOR && portal1.name().equals(ENTRANCE_EXIT_NAME)) {
            return OptionalInt.empty();
          }

          if (portal1.direction() == portal2.direction()) {
            return OptionalInt.empty();
          }

          // ENTER portals can only goto their matching EXIT
          if (!portal1.name().equals(ENTRANCE_EXIT_NAME) && portal1.name().equals(portal2.name())) {
            if (portal1.direction() == Portal.Direction.ENTER && portal1.edge() != portal2.edge()) {
              return OptionalInt.of(1);
            }

            return OptionalInt.empty();
          }

          if (portal1.direction() == Portal.Direction.ENTER) {
            return OptionalInt.empty();
          }

          return findPathOnGrid(grid, portal1.position(), portal2.position());
        })
        .withDefaultIntAssignment()
        .build();

    var entranceNode = graph.getNode(P_ENTRANCE).map(DataIntGraphNode::getData).orElseThrow();
    var exitNode = graph.getNode(P_EXIT).map(DataIntGraphNode::getData).orElseThrow();

    var path = GraphUtil.aStar(
        entranceNode,
        exitNode,
        portal -> {
          int level = portal.level();

          if (level > 50) {
            return List.of();
          }

          if (portal.direction() == Portal.Direction.ENTER) {
            var exitPortal = graph
                .getNode(portal)
                .flatMap(AbsIntGraphNode::lastOutNode)
                .map(DataIntGraphNode::getData)
                .orElseThrow();

            return List.of(level == 0 ? exitPortal : new Portal(exitPortal, level));
          }

          var potentialNeighbors = graph
              .getNode(portal)
              .map(DataIntGraphNode::outNodes)
              .orElseThrow();

          var neighbors = new ArrayList<Portal>();
          if (level == 0) {
            for (DataIntGraphNode<Portal> node : potentialNeighbors) {
              if (node.getData().equals(exitNode)) {
                neighbors.add(node.getData());
              } else if (node.getData().edge() == Portal.Edge.INTERIOR) {
                neighbors.add(new Portal(node.getData(), 1));
              }
            }
          } else {
            for (DataIntGraphNode<Portal> node : potentialNeighbors) {
              if (node.getData().name().equals(ENTRANCE_EXIT_NAME)) {
                continue;
              }

              neighbors.add(new Portal(node.getData(), node.getData().edge() == Portal.Edge.EXTERIOR ? level - 1 : level + 1));
            }
          }

          return neighbors;
        },
        (ToIntFunction<Portal>) __ -> 0,
        (left, right) -> graph.getCost(graph.getNode(left).orElseThrow(), graph.getNode(right).orElseThrow())
    );

    return path.map(GraphPath::length).orElseThrow();
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

  private static OptionalInt findPathOnGrid(Grid grid, Point2D start, Point2D end) {
    return GraphUtil
        .aStar(
            start,
            end,
            pos -> GridUtil
                .orthogonalNeighbors(grid, pos)
                .stream()
                .filter(neighbor -> grid.configuration().isEmptySpace(grid.get(neighbor)) || neighbor.equals(end))
                .collect(Collectors.toList())
        )
        .map(GraphPath::length)
        .map(length -> OptionalInt.of(length - 2))
        .orElseGet(OptionalInt::empty);
  }

  private static record Portal(Point2D position,
                               String name,
                               Edge edge,
                               Direction direction,
                               int level) {
    private enum Edge {
      INTERIOR, EXTERIOR
    }

    private enum Direction {
      ENTER, EXIT
    }

    public Portal(@NotNull Portal portal, Point2D newPosition) {
      this(newPosition, portal.name, portal.edge, portal.direction, portal.level);
    }

    public Portal(@NotNull Portal portal, int newLevel) {
      this(portal.position, portal.name, portal.edge, portal.direction, newLevel);
    }

    public Portal(@NotNull Point2D position,
                  @NotNull String portalStr,
                  @NotNull Direction direction) {
      this(position,
          portalStr.substring(0, 2),
          portalStr.substring(3).equals("I") ? Portal.Edge.INTERIOR : Portal.Edge.EXTERIOR,
          direction,
          0
      );
    }
  }
}
