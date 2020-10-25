package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.AdventUtil;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day3 extends AdventSolution2018<Long, String> {
  public static void main(String[] args) {
    new Day3().runAndPrintAll();
  }

  @Override
  public Long part1(BufferedReader reader) {
    return reader
        .lines()
        .map(Claim::newClaim)
        .collect(Collectors.collectingAndThen(
            Collectors.toList(),
            Day3::tallyClaims
        ))
        .values()
        .stream()
        .filter(i -> i > 1)
        .count();
  }

  @Override
  public String part2(BufferedReader reader) {
    List<Claim> claims = reader
        .lines()
        .map(Claim::newClaim)
        .collect(Collectors.toList());

    Map<Point2D, Integer> claimMap = tallyClaims(claims);

    for (Claim claim : claims) {
      boolean hasOverlap = false;

      for (int i = claim.topLeft().x(); i <= claim.bottomRight().x(); i++) {
        for (int j = claim.topLeft().y(); j <= claim.bottomRight().y(); j++) {
          Point2D claimPoint = new Point2D(i, j);
          hasOverlap |= claimMap.get(claimPoint) != 1;
        }
      }

      if (!hasOverlap) {
        return claim.id();
      }
    }

    throw new IllegalArgumentException();
  }

  private static Map<Point2D, Integer> tallyClaims(List<Claim> claims) {
    var claimMap = new HashMap<Point2D, Integer>();

    for (Claim claim : claims) {
      for (int i = claim.topLeft().x(); i <= claim.bottomRight().x(); i++) {
        for (int j = claim.topLeft().y(); j <= claim.bottomRight().y(); j++) {
          AdventUtil.incrementMap(claimMap, new Point2D(i, j));
        }
      }
    }

    return claimMap;
  }

  private static record Claim(String id, Point2D topLeft, Point2D bottomRight) {
    public static Claim newClaim(String claim) {
      var segments = claim.split(" ");

      segments[2] = segments[2].substring(0, segments[2].length() - 1);
      String[] pos = segments[2].split(",");
      String[] size = segments[3].split("x");

      var posX = Integer.parseInt(pos[0]);
      var posY = Integer.parseInt(pos[1]);

      return new Claim(
          segments[0],
          new Point2D(
              posX + 1,
              posY + 1
          ),
          new Point2D(
              posX + Integer.parseInt(size[0]),
              posY + Integer.parseInt(size[1])
          )
      );
    }
  }
}
