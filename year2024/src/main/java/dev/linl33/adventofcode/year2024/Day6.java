package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Day6 extends AdventSolution2024<Integer, Integer> implements ByteBufferAdventSolution<Integer, Integer>, NullBufferedReaderSolution<Integer, Integer> {
  public static void main() {
    new Day6().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Integer part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Integer part1(@NotNull ByteBuffer byteBuffer) {
    var map = new byte[byteBuffer.limit()];
    byteBuffer.get(map);
    var dim = ((int) Math.sqrt(4 * byteBuffer.limit() + 1) - 1) / 2;

    var guardIdx = locateGuard(map);
    var guardX = guardIdx % (dim + 1);
    var guardY = guardIdx / (dim + 1);

    var visited = new boolean[map.length];
    var visitedCount = 0;

    var deltaX = 0;
    var deltaY = -1;

    while (guardX >= 0 && guardX < dim && guardY >= 0 && guardY < dim) {
      var currIdx = guardX + guardY * (dim + 1);
      if (map[currIdx] == '#') {
        guardX -= deltaX;
        guardY -= deltaY;

        // turn right
        var tmp = deltaX;
        deltaX = -deltaY;
        deltaY = tmp;
      } else {
        if (!visited[currIdx]) {
          visitedCount++;
        }
        visited[currIdx] = true;
      }

      guardX += deltaX;
      guardY += deltaY;
    }

    return visitedCount;
  }

  @Override
  public Integer part2(@NotNull ByteBuffer byteBuffer) {
    var map = new byte[byteBuffer.limit()];
    byteBuffer.get(map);
    var dim = ((int) Math.sqrt(4 * map.length + 1) - 1) / 2;

    var guardIdx = locateGuard(map);
    var guardX = guardIdx % (dim + 1);
    var guardY = guardIdx / (dim + 1);

    var deltaX = 0;
    var deltaY = -1;

    var visitedMap = new boolean[map.length];
    var visited = new int[map.length];
    var visitedCount = 0;

    // TODO: this loop can be combined with the other loop
    while (guardX >= 0 && guardX < dim && guardY >= 0 && guardY < dim) {
      var currIdx = guardX + guardY * (dim + 1);
      if (map[currIdx] == '#') {
        guardX -= deltaX;
        guardY -= deltaY;

        // turn right
        var tmp = deltaX;
        deltaX = -deltaY;
        deltaY = tmp;
      } else {
        if (!visitedMap[currIdx]) {
          visited[visitedCount++] = (currIdx << 2) | (deltaX & 1) | (((deltaX | deltaY) >>> 31) << 1);
        }
        visitedMap[currIdx] = true;
      }

      guardX += deltaX;
      guardY += deltaY;
    }

    var count = 0;
    var seenStates = new boolean[(map.length - 1) << 2 + 1];

    for (var i = 1; i < visitedCount; i++) {
      var obstacleIdx = visited[i] >> 2;

      var delta = (visited[i] & 2) == 0 ? 1 : -1;
      deltaX = 0;
      deltaY = 0;
      if ((visited[i] & 1) != 0) {
        deltaX = delta;
      } else {
        deltaY = delta;
      }

      guardX = obstacleIdx % (dim + 1);
      guardY = obstacleIdx / (dim + 1);

      Arrays.fill(seenStates, false);

      while (guardX >= 0 && guardX < dim && guardY >= 0 && guardY < dim) {
        var currIdx = guardX + guardY * (dim + 1);
        if (map[currIdx] == '#' || currIdx == obstacleIdx) {
          var state = (currIdx << 2) | (deltaX & 1) | (((deltaX | deltaY) >>> 31) << 1);

          if (seenStates[state]) {
            count++;
            break;
          }
          seenStates[state] = true;

          guardX -= deltaX;
          guardY -= deltaY;

          // turn right
          var tmp = deltaX;
          deltaX = -deltaY;
          deltaY = tmp;
        }

        guardX += deltaX;
        guardY += deltaY;
      }
    }

    return count;
  }

  private static int locateGuard(byte[] arr) {
    for (var i = 0; i < arr.length; i++) {
      if (arr[i] == '^') {
        return i;
      }
    }

    return -1;
  }
}
