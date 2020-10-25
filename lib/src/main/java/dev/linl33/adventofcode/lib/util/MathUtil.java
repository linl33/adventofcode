package dev.linl33.adventofcode.lib.util;

import dev.linl33.adventofcode.lib.point.Point;
import dev.linl33.adventofcode.lib.point.Point2D;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class MathUtil {
  public static int gcd(int a, int b) {
    if (a == b) {
      return a;
    }

    if (a == 0) {
      return b;
    }

    if (b == 0) {
      return a;
    }

    if (a % 2 == 0) {
      if (b % 2 != 0) {
        return gcd(a >> 1, b);
      } else {
        return gcd(a >> 1, b >> 1) << 1;
      }
    }

    if (b % 2 == 0) {
      return gcd(a, b >> 1);
    }

    if (a > b) {
      return gcd((a - b) >> 1, b);
    }

    return gcd((b - a) >> 1, a);
  }

  public static long gcd(long a, long b) {
    if (a == b) {
      return a;
    }

    if (a == 0) {
      return b;
    }

    if (b == 0) {
      return a;
    }

    if (a % 2L == 0) {
      if (b % 2L != 0) {
        return gcd(a >> 1, b);
      } else {
        return gcd(a >> 1, b >> 1) << 1;
      }
    }

    if (b % 2L == 0) {
      return gcd(a, b >> 1);
    }

    if (a > b) {
      return gcd((a - b) >> 1, b);
    }

    return gcd((b - a) >> 1, a);
  }

  public static int lcm(int... numbers) {
    var result = lcm(numbers[0], numbers[1]);
    for (int i = 2; i < numbers.length; i++) {
      result = lcm(result, numbers[i]);
    }

    return result;
  }

  public static int lcm(int a, int b) {
    if (a == 0 && b == 0) {
      return 0;
    }

    return Math.abs(a) / gcd(a, b) * Math.abs(b);
  }

  public static long lcm(long... numbers) {
    var result = lcm(numbers[0], numbers[1]);
    for (int i = 2; i < numbers.length; i++) {
      result = lcm(result, numbers[i]);
    }

    return result;
  }

  public static long lcm(long a, long b) {
    if (a == 0 && b == 0) {
      return 0;
    }

    return Math.abs(a) / gcd(a, b) * Math.abs(b);
  }

  /**
   *
   * @param a
   * @param b
   * @return y-intercept as Point2D.x, slope as Point2D.y
   */
  public static Point2D makeLine(Point2D a, Point2D b) {
    var slope = (b.y() - a.y()) / (b.x() - a.x());
    var intercept = a.y() - slope * a.x();

    return new Point2D(intercept, slope);
  }
}
