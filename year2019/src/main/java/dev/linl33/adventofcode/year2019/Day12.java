package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.point.Point3D;
import dev.linl33.adventofcode.lib.util.GeomUtil;
import dev.linl33.adventofcode.lib.util.MathUtil;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.ToIntFunction;

public class Day12 extends AdventSolution2019<Integer, Long> {
  public static void main(String[] args) {
    new Day12().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    var moonArr = parseInitialPosition(reader);
    var velocityArr = buildInitialVelocity(moonArr);

    for (var i = 0; i < 1000; i++) {
      simulate(moonArr, velocityArr);
    }

    return computeEnergy(moonArr, velocityArr);
  }

  @Override
  public Long part2(BufferedReader reader) {
    var moonArr = parseInitialPosition(reader);
    var velocityArr = buildInitialVelocity(moonArr);

    var windowSize = 25;

    var windowRefX = new ArrayList<Integer>(windowSize);
    var windowCurrX = new LinkedList<Integer>();

    var windowRefY = new ArrayList<Integer>(windowSize);
    var windowCurrY = new LinkedList<Integer>();

    var windowRefZ = new ArrayList<Integer>(windowSize);
    var windowCurrZ = new LinkedList<Integer>();

    var foundX = false;
    var foundY = false;
    var foundZ = false;

    var periodX = 0L;
    var periodY = 0L;
    var periodZ = 0L;

    var time = 0L;
    while (!foundX || !foundY || !foundZ) {
      simulate(moonArr, velocityArr);

      int eX = computeEnergyX(moonArr, velocityArr);
      int eY = computeEnergyY(moonArr, velocityArr);
      int eZ = computeEnergyZ(moonArr, velocityArr);
      if (time < windowSize) {
        windowRefX.add(eX);
        windowRefY.add(eY);
        windowRefZ.add(eZ);
      } else {
        if (windowCurrX.size() >= windowSize) {
          windowCurrX.removeFirst();
        }

        if (windowCurrY.size() >= windowSize) {
          windowCurrY.removeFirst();
        }

        if (windowCurrZ.size() >= windowSize) {
          windowCurrZ.removeFirst();
        }

        windowCurrX.add(eX);
        windowCurrY.add(eY);
        windowCurrZ.add(eZ);

        if (!foundX && windowRefX.equals(windowCurrX)) {
          foundX = true;
          periodX = time - windowSize;
        }

        if (!foundY && windowRefY.equals(windowCurrY)) {
          foundY = true;
          periodY = time - windowSize;
        }

        if (!foundZ && windowRefZ.equals(windowCurrZ)) {
          foundZ = true;
          periodZ = time - windowSize;
        }
      }

      time++;
    }

    return MathUtil.lcm(periodX + 1, periodY + 1, periodZ + 1);
  }

  private static Point3D[] parseInitialPosition(BufferedReader reader) {
    return reader
        .lines()
        .map(line -> line.substring(1, line.length() - 1).split(", "))
        .map(segments -> new Point3D(
            segments[0].split("=")[1],
            segments[1].split("=")[1],
            segments[2].split("=")[1]
        ))
        .toArray(Point3D[]::new);
  }

  private static Point3D[] buildInitialVelocity(Point3D[] moonArr) {
    var velArr = new Point3D[moonArr.length];
    for (var i = 0; i < moonArr.length; i++) {
      velArr[i] = new Point3D(0, 0, 0);
    }

    return velArr;
  }

  private static void simulate(Point3D[] moonArr, Point3D[] velocityArr) {
    var newMoonArr = new Point3D[moonArr.length];

    for (var moonIdx = 0; moonIdx < moonArr.length; moonIdx++) {
      var moon = moonArr[moonIdx];

      var velDeltaX = 0;
      var velDeltaY = 0;
      var velDeltaZ = 0;

      for (var anotherMoonIdx = 0; anotherMoonIdx < moonArr.length; anotherMoonIdx++) {
        if (anotherMoonIdx == moonIdx) {
          continue;
        }

        var anotherMoon = moonArr[anotherMoonIdx];

        velDeltaX += Integer.compare(anotherMoon.x(), moon.x());
        velDeltaY += Integer.compare(anotherMoon.y(), moon.y());
        velDeltaZ += Integer.compare(anotherMoon.z(), moon.z());
      }

      var velocity = velocityArr[moonIdx].translate(velDeltaX, velDeltaY, velDeltaZ);
      velocityArr[moonIdx] = velocity;
      newMoonArr[moonIdx] = moon.translate(velocity);
    }

    System.arraycopy(newMoonArr, 0, moonArr, 0, newMoonArr.length);
  }

  private static int computeEnergy(Point3D[] position, Point3D[] velocity, ToIntFunction<Point3D> extractEnergyFunc) {
    var total = 0;
    for (var i = 0; i < position.length; i++) {
      total += extractEnergyFunc.applyAsInt(position[i]) * extractEnergyFunc.applyAsInt(velocity[i]);
    }

    return total;
  }

  private static int computeEnergy(Point3D[] position, Point3D[] velocity) {
    return computeEnergy(position, velocity, GeomUtil::manhattanDistToOrigin);
  }

  private static int computeEnergyX(Point3D[] position, Point3D[] velocity) {
    return computeEnergy(position, velocity, pt -> Math.abs(pt.x()));
  }

  private static int computeEnergyY(Point3D[] position, Point3D[] velocity) {
    return computeEnergy(position, velocity, pt -> Math.abs(pt.y()));
  }

  private static int computeEnergyZ(Point3D[] position, Point3D[] velocity) {
    return computeEnergy(position, velocity, pt -> Math.abs(pt.z()));
  }
}
