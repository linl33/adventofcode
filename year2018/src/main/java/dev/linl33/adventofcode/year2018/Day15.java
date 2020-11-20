package dev.linl33.adventofcode.year2018;

import dev.linl33.adventofcode.lib.GridEntity;
import dev.linl33.adventofcode.lib.point.Point2D;

import java.io.BufferedReader;

public class Day15 extends AdventSolution2018<Integer, Integer> {
  public static void main(String[] args) {
    new Day15().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return -1;
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return -1;
  }

  private static record CaveEntity(String id,
                                   Point2D position,
                                   Day15.CaveEntity.Team team,
                                   int hp,
                                   int attackPower) implements GridEntity {
    public enum Team {
      ELF, GOBLIN
    }

    @Override
    public String getId() {
      return id();
    }

    @Override
    public Point2D getPosition() {
      return position();
    }
  }
}
