package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.util.MathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Day9 extends AdventSolution2024<Long, Long> {
  public static void main() {
    new Day9().runAndPrintAll();
  }

  @Override
  public Long part1(@NotNull BufferedReader reader) throws IOException {
    var line = reader.readLine() + '0';

    var disk = new short[line.length() * 9];
    var diskSize = 0;

    for (short i = 0; i < line.length(); i += 2) {
      var size = line.charAt(i) - '0';
      var free = line.charAt(i + 1) - '0';

      Arrays.fill(disk, diskSize, diskSize + size, (short) (i / 2));
      diskSize += size + free;
    }

    for (int firstEmpty = (line.charAt(0) - '0'); firstEmpty < diskSize; firstEmpty++) {
      if (disk[firstEmpty] != 0) {
        continue;
      }

      while (disk[diskSize - 1] == 0) {
        diskSize--;
      }

      disk[firstEmpty] = disk[diskSize-- - 1];
    }

    var sum = 0L;
    for (var i = 0; i < diskSize; i++) {
      sum += i * disk[i];
    }

    return sum;
  }

  @Override
  public Long part2(@NotNull BufferedReader reader) throws IOException {
    var line = reader.readLine();

    var disk = new ArrayList<int[]>();
    for (var i = 0; i < line.length(); i += 2) {
      var size = line.charAt(i) - '0';
      var free = i + 1 == line.length() ? 0 : line.charAt(i + 1) - '0';
      var id = i / 2;

      disk.add(new int[] { id, size });
      if (free != 0) {
        disk.add(new int[] { -1, free });
      }
    }

    var currId = line.length() / 2;
    for (var i = disk.size() - 1; i >= 0; i--) {
      var f = disk.get(i);
      if (f[0] != currId) {
        continue;
      }

      currId--;

      for (var j = 0; j < i; j++) {
        var f2 = disk.get(j);
        if (f2[0] == -1 && f2[1] >= f[1]) {
          var rem = f2[1] - f[1];

          f2[0] = f[0];
          f2[1] = f[1];
          f[0] = -1;

          if (rem != 0) {
            disk.add(j + 1, new int[] { -1, rem });
            i++;
          }

          break;
        }
      }
    }

    var sum = 0L;
    var idx = 0L;

    for (var i = 0; i < disk.size(); i++) {
      var f = disk.get(i);
      if (f[0] != -1) {
        sum += (MathUtil.triangularNumber(idx + f[1] - 1) - MathUtil.triangularNumber(idx - 1)) * f[0];
      }
      idx += f[1];
    }

    return sum;
  }
}
