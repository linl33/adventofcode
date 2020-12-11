package dev.linl33.adventofcode.lib.grid;

import java.io.BufferedReader;

public class RowArrayGrid implements ArrayGrid {
  private final int[][] gridArray;
  private final int width;
  private final int height;

  public RowArrayGrid(int[][] gridArray) {
    this.gridArray = gridArray;
    this.height = gridArray.length;
    this.width = gridArray[0].length;
  }

  public RowArrayGrid(BufferedReader reader) {
    this(reader
        .lines()
        .map(line -> line.chars().toArray())
        .toArray(int[][]::new));
  }

  public RowArrayGrid(int height, int width) {
    this(new int[height][width]);
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
    return width;
  }

  @Override
  public int height() {
    return height;
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
