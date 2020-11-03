package dev.linl33.adventofcode.lib.grid;

import java.io.BufferedReader;

public class ColumnArrayGrid implements ArrayGrid {
  private final int[][] gridArray;

  public ColumnArrayGrid(int[][] gridArray) {
    this.gridArray = gridArray;
  }

  public ColumnArrayGrid(BufferedReader reader) {
    this(new RowArrayGrid(reader).asColumnArrayGrid().array());
  }

  @Override
  public int[][] array() {
    return gridArray;
  }

  @Override
  public int get(int x, int y) {
    return gridArray[x][y];
  }

  @Override
  public void set(int x, int y, int value) {
    gridArray[x][y] = value;
  }

  @Override
  public int width() {
    return gridArray.length;
  }

  @Override
  public int height() {
    return gridArray[0].length;
  }

  @Override
  public int[] row(int y) {
    return null;
  }

  @Override
  public int[] column(int x) {
    return gridArray[x];
  }
}
