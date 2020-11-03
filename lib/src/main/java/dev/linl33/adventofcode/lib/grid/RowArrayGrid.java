package dev.linl33.adventofcode.lib.grid;

import java.io.BufferedReader;

public class RowArrayGrid implements ArrayGrid {
  private final int[][] gridArray;

  public RowArrayGrid(int[][] gridArray) {
    this.gridArray = gridArray;
  }

  public RowArrayGrid(BufferedReader reader) {
    this(reader
        .lines()
        .map(line -> {
          var charArr = line.toCharArray();
          var intArr = new int[line.length()];

          for (int i = 0; i < charArr.length; i++) {
            intArr[i] = charArr[i];
          }

          return intArr;
        })
        .toArray(int[][]::new));
  }

  public ColumnArrayGrid asColumnArrayGrid() {
    var columnArray = new int[width()][height()];

    visit((GridConsumer) (x, y, value) -> columnArray[x][y] = value);

    return new ColumnArrayGrid(columnArray);
  }

  @Override
  public int[][] array() {
    return gridArray;
  }

  @Override
  public int get(int x, int y) {
    return gridArray[y][x];
  }

  @Override
  public void set(int x, int y, int value) {
    gridArray[y][x] = value;
  }

  @Override
  public int width() {
    return gridArray[0].length;
  }

  @Override
  public int height() {
    return gridArray.length;
  }

  @Override
  public int[] row(int y) {
    return gridArray[y];
  }

  @Override
  public int[] column(int x) {
    return null;
  }
}
