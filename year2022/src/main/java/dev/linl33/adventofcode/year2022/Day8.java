package dev.linl33.adventofcode.year2022;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;

public class Day8 extends AdventSolution2022<Integer, Integer> {
  public static void main(String[] args) {
    new Day8().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull BufferedReader reader) {
    var input = reader.lines().toArray(String[]::new);

    var firstRow = input[0];
    var cols = firstRow.length();
    var rows = input.length;
    var lastRow = input[rows - 1];

    var visible = new boolean[cols * rows];
    var visibleTrees = cols * 2 + (rows - 2) * 2;

    for (int y = 1; y < rows - 1; y++) {
      var row = input[y];
      var rowOffset = y * cols;

      var max = row.charAt(0);
      var maxX = 0;

      // left-right
      for (int x = 1; x < cols - 1; x++) {
        var curr = row.charAt(x);

        if (curr > max) {
          max = curr;
          maxX = x;
          visible[x + rowOffset] = true;
          visibleTrees++;
        }
      }

      // right-left
      max = row.charAt(cols - 1);
      for (int x = cols - 2; x > maxX; x--) {
        var curr = row.charAt(x);

        if (curr > max) {
          max = curr;
          visible[x + rowOffset] = true;
          visibleTrees++;
        }
      }
    }

    for (int x = 1; x < cols - 1; x++) {
      var max = firstRow.charAt(x);
      var maxY = 0;

      // top-down
      for (int y = 1; y < rows - 1; y++) {
        var curr = input[y].charAt(x);

        if (curr > max) {
          max = curr;
          maxY = y;
          visibleTrees += visible[x + y * cols] ? 0 : 1;
        }
      }

      // bottom-up
      max = lastRow.charAt(x);
      for (int y = rows - 2; y > maxY; y--) {
        var curr = input[y].charAt(x);
        if (curr > max) {
          max = curr;
          visibleTrees += visible[x + y * cols] ? 0 : 1;
        }
      }
    }

    return visibleTrees;
  }

  @Override
  public Integer part2(@NotNull BufferedReader reader) {
    var input = reader.lines().toArray(String[]::new);

    var cols = input[0].length();
    var rows = input.length;
    var max = -1;
    var colBackRef = new int[cols * rows];
    var colBackRefPointer = cols - 1;
    var skippedColumns = new boolean[cols];

    for (int treeY = 1; treeY < rows - 1; treeY++) {
      colBackRefPointer = treeY * cols;

      var row = input[treeY];
      var backRef = new int[cols];

      for (int treeX = 1; treeX < cols - 1; treeX++) {
        colBackRefPointer++;
        var tree = row.codePointAt(treeX);
        var score = 1;

        // look right
        var x = treeX + 1;
        for (; x < cols; x++) {
          var t = row.codePointAt(x);
          if (t >= tree) {
            if (t == tree) {
              backRef[x] = treeX;
            }
            x++;
            break;
          }

          backRef[x] = treeX;
        }
        score *= (x - 1) - treeX;
        // look left
        score *= treeX - backRef[treeX];

        var maxPotential = (treeY - colBackRef[colBackRefPointer]) * ((rows - 1) - treeY) * score;
        if (maxPotential <= max) {
          skippedColumns[treeY] = true;
          continue;
        }

        // look up
        var y = treeY + 1;
        var colBackRefWritePointer = colBackRefPointer + cols;
        for (; y < rows; y++) {
          var t = input[y].codePointAt(treeX);
          if (t >= tree) {
            if (t == tree) {
              colBackRef[colBackRefWritePointer] = treeY;
            }

            y++;
            break;
          }

          colBackRef[colBackRefWritePointer] = treeY;
          colBackRefWritePointer += cols;
        }
        score *= (y - 1) - treeY;

        // look down
        if (skippedColumns[treeY]) {
          // if this column had been skipped, colBackRef can only provide a lower bound not the exact index
          for (y = treeY - 1; y >= colBackRef[colBackRefPointer]; y--) {
            var t = input[y].codePointAt(treeX);
            if (t >= tree) {
              y--;
              break;
            }
          }
          score *= treeY - (y + 1);
        } else {
          score *= treeY - colBackRef[colBackRefPointer];
        }

        max = Math.max(max, score);

        // colBackRef is correct again, re-enable fast path
        skippedColumns[treeY] = false;
      }
    }

    return max;
  }
}
