package dev.linl33.adventofcode.lib.grid;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Objects;

public class ColumnArrayGrid implements ArrayGrid {
  private final int[] gridArray;
  private final int width;
  private final int height;

  public ColumnArrayGrid(int[] gridArray, int height, int width) {
    this.gridArray = gridArray;
    this.height = height;
    this.width = width;
  }

  public ColumnArrayGrid(BufferedReader reader) {
    var columnArrayGrid = new RowArrayGrid(reader).asColumnArrayGrid();
    this.gridArray = columnArrayGrid.array();
    this.width = columnArrayGrid.width;
    this.height = columnArrayGrid.height;
  }

  public ColumnArrayGrid(int height, int width) {
    this(new int[width * height], height, width);
  }

  @Override
  public int[] array() {
    return gridArray;
  }

  @Override
  public int get(int x, int y) {
    return gridArray[x * height + y];
  }

  @Override
  public void set(int x, int y, int value) {
    gridArray[x * height + y] = value;
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
    return null;
  }

  @Override
  public int[] column(int x) {
    return Arrays.copyOfRange(gridArray, x * height, x * height + height);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ColumnArrayGrid that = (ColumnArrayGrid) o;
    return width == that.width && height == that.height && Arrays.equals(gridArray, that.gridArray);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(width, height);
    result = 31 * result + Arrays.hashCode(gridArray);
    return result;
  }
}
