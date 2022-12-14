package dev.linl33.adventofcode.lib.grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class RowArrayGrid implements ArrayGrid {
  private final int[] gridArray;
  private final int width;
  private final int height;

  public RowArrayGrid(int[] gridArray, int height, int width) {
    this.gridArray = gridArray;
    this.height = height;
    this.width = width;
  }

  public RowArrayGrid(BufferedReader reader) {
    String line;

    try {
      line = reader.readLine();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    var gridArray = Stream
        .concat(
            Stream.of(line),
            reader.lines()
        )
        .flatMapToInt(String::chars)
        .toArray();

    this.gridArray = gridArray;
    this.width = line.length();
    this.height = gridArray.length / line.length();
  }

  public RowArrayGrid(int height, int width) {
    this(new int[height * width], height, width);
  }

  public ColumnArrayGrid asColumnArrayGrid() {
    var columnArray = new int[width() * height()];

    visit((GridConsumer) (x, y, value) -> columnArray[x * height() + y] = value);
    return new ColumnArrayGrid(columnArray, height(), width());
  }

  @Override
  public int[] array() {
    return gridArray;
  }

  @Override
  public int get(int x, int y) {
    return gridArray[y * width + x];
  }

  @Override
  public void set(int x, int y, int value) {
    gridArray[y * width + x] = value;
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
    return Arrays.copyOfRange(gridArray, y * width, y * width + width);
  }

  @Override
  public int[] column(int x) {
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RowArrayGrid that = (RowArrayGrid) o;
    return width == that.width && height == that.height && Arrays.equals(gridArray, that.gridArray);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(width, height);
    result = 31 * result + Arrays.hashCode(gridArray);
    return result;
  }
}
