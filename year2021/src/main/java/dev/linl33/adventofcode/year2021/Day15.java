package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.graph.mutable.MutableGraphBuilder;
import dev.linl33.adventofcode.lib.graph.mutable.MutableGraphNode;
import dev.linl33.adventofcode.lib.grid.ArrayGrid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;

public class Day15 extends AdventSolution2021<Integer, Integer> {
  private static final int LARGE_GRID_MULTIPLIER = 5;

  public static void main(String[] args) {
    new Day15().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, UnaryOperator.identity());
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) throws Exception {
    return solve(reader, Day15::buildLargeGrid);
  }

  private static int solve(@NotNull BufferedReader reader,
                           @NotNull UnaryOperator<ArrayGrid> gridTransformOperator) {
    ArrayGrid grid = new RowArrayGrid(reader);

    for (int i = 0; i < grid.array().length; i++) {
      grid.array()[i] -= '0';
    }
    // TODO: avoid transforming the grid, calculate the expanded risk on the go
    grid = gridTransformOperator.apply(grid);

    // TODO: avoid building the graph, run aStar on the grid
    var graphBuilder = new MutableGraphBuilder();
    for (int y = 0; y < grid.height(); y++) {
      for (int x = 0; x < grid.width(); x++) {
        var currNode = x + "," + y;
        graphBuilder.addNode(currNode);

        if (x > 0) {
          var leftNode = (x - 1) + "," + y;
          graphBuilder.addNode(leftNode);
          graphBuilder.addEdge(currNode, leftNode, grid.get(x - 1, y));
        }

        if (x < grid.width() - 1) {
          var rightNode = (x + 1) + "," + y;
          graphBuilder.addNode(rightNode);
          graphBuilder.addEdge(currNode, rightNode, grid.get(x + 1, y));
        }

        if (y > 0) {
          var upNode = x + "," + (y - 1);
          graphBuilder.addNode(upNode);
          graphBuilder.addEdge(currNode, upNode, grid.get(x, y - 1));
        }

        if (y < grid.height() - 1) {
          var downNode = x + "," + (y + 1);
          graphBuilder.addNode(downNode);
          graphBuilder.addEdge(currNode, downNode, grid.get(x, y + 1));
        }
      }
    }

    var g = graphBuilder.build();
    var start = g.getNode("0,0").orElseThrow();
    var end = g.getNode((grid.width() - 1) + "," + (grid.height() - 1)).orElseThrow();

    var finalGrid = grid;
    var path = GraphUtil.aStar(
        start,
        end,
        MutableGraphNode::outNodes,
        (ToIntFunction<MutableGraphNode>) node -> {
          // manhattan distance to bottom-right as the heuristic

          var label = node.getLabel();
          var splitAt = label.indexOf(',', 1);
          var x = Integer.parseInt(label, 0, splitAt, 10);
          var y = Integer.parseInt(label, splitAt + 1, label.length(), 10);
          return finalGrid.width() - x - 1 + finalGrid.height() - y - 1;
        },
        g::getCost
    ).orElseThrow();

    return path.length();
  }

  @NotNull
  private static RowArrayGrid buildLargeGrid(@NotNull ArrayGrid grid) {
    var largeGrid = new RowArrayGrid(grid.height() * LARGE_GRID_MULTIPLIER, grid.width() * LARGE_GRID_MULTIPLIER);

    for (int y = 0; y < largeGrid.height(); y++) {
      for (int x = 0; x < largeGrid.width(); x++) {
        // the value is equal to the position in the original grid
        // plus the manhattan distance to the top-left tile
        var val = (
            grid.get(x % grid.width(), y % grid.height()) // value is equal to the position in the original grid
                + (y / grid.height()) + (x / grid.width()) // plus the manhattan distance to the top-left tile
                - 1) % 9 + 1; // wrap around to [1-9]

        largeGrid.set(x, y, val);
      }
    }

    // TODO: use arraycopy to skip identical tiles

    return largeGrid;
  }
}
