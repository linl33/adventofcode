package dev.linl33.adventofcode.year2021;

import dev.linl33.adventofcode.lib.grid.ArrayGrid;
import dev.linl33.adventofcode.lib.grid.RowArrayGrid;
import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Day13 extends AdventSolution2021<Integer, ArrayGrid> {
  private static final int MAX_HEIGHT = 2000;
  public static final int MAX_WIDTH = 2000;

  public static void main(String[] args) {
    new Day13().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) throws Exception {
    var parts = AdventUtil.readInputGrouped(reader).toList();
    var grid = parts.get(0).toArray(String[]::new);
    var instr = parts.get(1).toArray(String[]::new);

    var g = new RowArrayGrid(MAX_HEIGHT, MAX_WIDTH);
    for (var s : grid) {
      var splitAt = s.indexOf(',', 1);
      g.set(
          Integer.parseInt(s, 0, splitAt, 10),
          Integer.parseInt(s, splitAt + 1, s.length(), 10),
          '#'
      );
    }

    var folded = fold(g, Arrays.copyOf(instr, 1));

    var c = 0;
    for (int i : folded.array()) {
      if (i == '#') {
        c++;
      }
    }

    return c;
  }

  @Override
  public ArrayGrid part2(@NotNull BufferedReader reader) throws Exception {
    var parts = AdventUtil.readInputGrouped(reader).toList();
    var grid = parts.get(0).toArray(String[]::new);
    var instr = parts.get(1).toArray(String[]::new);

    var g = new RowArrayGrid(MAX_HEIGHT, MAX_WIDTH);
    for (var s : grid) {
      var splitAt = s.indexOf(',', 1);
      g.set(
          Integer.parseInt(s, 0, splitAt, 10),
          Integer.parseInt(s, splitAt + 1, s.length(), 10),
          '#'
      );
    }

    return fold(g, instr);
  }

  @Override
  public Object part2PrintMapping(@NotNull ArrayGrid part2Result) {
    var lines = new ArrayList<String>();
    part2Result.print(lines::add);
    return lines;
  }

  private static ArrayGrid fold(@NotNull ArrayGrid g, @NotNull String[] foldingInstructions) {
    var height = g.height();
    var width = g.width();

    var maxHeight = height;
    var maxWidth = width;
    var lastRow = maxWidth * (maxHeight - 1);

    for (var instruction : foldingInstructions) {
      var axis = instruction.charAt(11);
      var axisVal = Integer.parseInt(instruction, 13, instruction.length(), 10);

      var colOffset = axisVal << 1;
      var rowOffset = (axisVal * maxWidth) << 1;

      if (axis == 'x') {
        for (int pos = 0; pos <= lastRow; pos += maxWidth) {
          var posMirror = pos + colOffset;
          for (int col = 0; col < axisVal; col++) {
            if (g.array()[posMirror - col] != '#') {
              continue;
            }

            g.array()[pos + col] = '#';
          }
        }
        width = axisVal;
      } else {
        for (int pos = 0; pos <= maxWidth * (axisVal - 1); pos += maxWidth) {
          var posMirror = rowOffset - pos;
          for (int col = 0; col < width; col++) {
            if (g.array()[posMirror + col] != '#') {
              continue;
            }

            g.array()[pos + col] = '#';
          }
        }
        height = axisVal;
      }
    }

    var fittedArray = new int[height * width];
    for (int row = 0; row < height; row++) {
      System.arraycopy(g.array(), row * maxWidth, fittedArray, row * width, width);
    }

    return new RowArrayGrid(fittedArray, height, width);
  }
}
