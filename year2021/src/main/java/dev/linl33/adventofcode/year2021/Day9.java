package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.graph.GraphUtil;
import dev.linl33.adventofcode.lib.graph.mutable.MutableGraphBuilder;
import dev.linl33.adventofcode.lib.graph.mutable.MutableGraphNode;
import dev.linl33.adventofcode.lib.grid.ArrayGrid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.ToIntFunction;

public class Day9 extends AdventSolution2021<Integer, Integer> {
  public static void main(String[] args) {
    new Day9().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var width = lines[0].length();
    var height = lines.length;
    var grid = new RowArrayGrid(height + 2, width + 2);
    Arrays.fill(grid.array(), '9');

    for (int i = 0; i < height; i++) {
      var line = lines[i];

      for (int j = 0; j < width; j++) {
        grid.set(j + 1, i + 1, line.codePointAt(j));
      }
    }

    var sum = 0;

    for (int row = 1; row <= height; row++) {
      for (int col = 1; col <= width; col++) {
        var center = grid.get(col, row);
        if (center == '9') {
          continue;
        }

        var left = grid.get(col - 1, row);
        var right = grid.get(col + 1, row);
        var up = grid.get(col, row - 1);
        var down = grid.get(col, row + 1);

        if (center < left && center < right && center < up && center < down) {
          sum += center - '0' + 1;
        }
      }
    }

    return sum;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var lines = reader.lines().toArray(String[]::new);
    var width = lines[0].length();
    var height = lines.length;
    var grid = new RowArrayGrid(height, width);
    Arrays.fill(grid.array(), 10);

    var basins = new ArrayList<Integer>();
    var graphBuilder = new MutableGraphBuilder();

    var remaining = new ArrayList<String>();

    for (int i = 0; i < height; i++) {
      var line = lines[i];

      for (int j = 0; j < line.length(); j++) {
        grid.set(j, i, line.charAt(j) - '0');
        if (line.charAt(j) != '9') {
          graphBuilder.addNode(j + "," + i);
          remaining.add(j + "," + i);
        }
      }
    }

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        if (grid.get(col, row) == 9) {
          continue;
        }

        var left = safeGet(grid, col - 1, row);
        var right = safeGet(grid, col + 1, row);
        var up = safeGet(grid, col, row - 1);
        var down = safeGet(grid, col, row + 1);

        var center = col + "," + row;
        if (left < 9) {
          graphBuilder.addEdge(center, (col - 1) + "," + row, 1);
        }

        if (right < 9) {
          graphBuilder.addEdge(center, (col + 1) + "," + row, 1);
        }

        if (up < 9) {
          graphBuilder.addEdge(center, col + "," + (row - 1), 1);
        }

        if (down < 9) {
          graphBuilder.addEdge(center, col + "," + (row + 1), 1);
        }
      }
    }

    var g = graphBuilder.build();

    while (!remaining.isEmpty()) {
      var area = 1;
      var next = remaining.remove(0);
      var nextNode = g.getNode(next).orElseThrow();
      var toRemove = new ArrayList<String>();
      for (String s : remaining) {
        if (s.equals(next)) {
          continue;
        }

        if (GraphUtil.aStar(nextNode, g.getNode(s).orElseThrow(), MutableGraphNode::outNodes, (ToIntFunction<MutableGraphNode>) (__ -> 0), g::getCost).isPresent()) {
          area++;
          toRemove.add(s);
        }
      }
      remaining.removeAll(toRemove);
      basins.add(area);
    }

    return basins
        .stream()
        .sorted(Comparator.reverseOrder())
        .limit(3)
        .reduce(1, (a, b) -> a * b);
  }

  private static int safeGet(ArrayGrid grid, int x, int y) {
    if (x < 0 || x >= grid.width()) {
      return 10;
    }

    if (y < 0 || y >= grid.height()) {
      return 10;
    }

    return grid.get(x, y);
  }
}
