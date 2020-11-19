package dev.linl33.adventofcode.lib.util;

import dev.linl33.adventofcode.lib.point.Point2D;
import org.jetbrains.annotations.Range;

public final class MathUtil {
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

  @Range(from = 0, to = Integer.MAX_VALUE)
  public static int choose(@Range(from = 0, to = Integer.MAX_VALUE) int n,
                           @Range(from = 0, to = Integer.MAX_VALUE) int r) {
    if (n < r) {
      return 0;
    }

    if (r == 0 || r == n) {
      return 1;
    }

    if (r == 1) {
      return n;
    }

    return choose(n - 1, r - 1) + choose(n - 1, r);
  }
}
