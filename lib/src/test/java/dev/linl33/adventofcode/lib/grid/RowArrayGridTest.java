package dev.linl33.adventofcode.lib.grid;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class RowArrayGridTest {
  @Test
  void testEmptyGridCreation() {
    var grid = new RowArrayGrid(10, 20);

    assertEquals(20, grid.width());
    assertEquals(10, grid.height());

    for (int y = 0; y < 10; y++) {
      for (int x = 0; x < 10; x++) {
        assertEquals(0, grid.get(x, y));
      }
    }

    assertArrayEquals(new int[10 * 20], grid.array());
  }

  @Test
  void testGridCreation() {
    var gridStr = """
        123
                
        456
                
        789
        """;

    var grid = new RowArrayGrid(new BufferedReader(new StringReader(gridStr)));

    assertEquals(3, grid.width());
    assertEquals(3, grid.height());

    var val = '1';
    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        assertEquals(val++, grid.get(x, y));
      }
    }

    assertArrayEquals(new int[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'}, grid.array());
  }

  @Test
  void testGridTranspose() {
    var gridStr = """
        147
                
        258
                
        369
        """;

    var rowArrayGrid = new RowArrayGrid(new BufferedReader(new StringReader(gridStr)));
    var colArrayGrid = rowArrayGrid.asColumnArrayGrid();

    for (int y = 0; y < 3; y++) {
      for (int x = 0; x < 3; x++) {
        assertEquals(rowArrayGrid.get(x, y), colArrayGrid.get(x, y));
      }
    }

    assertArrayEquals(new int[] {'1', '2', '3', '4', '5', '6', '7', '8', '9'}, colArrayGrid.array());
  }
}
