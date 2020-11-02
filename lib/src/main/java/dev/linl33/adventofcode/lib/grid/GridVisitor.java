package dev.linl33.adventofcode.lib.grid;

@FunctionalInterface
public interface GridVisitor {
  default GridVisitResult preVisitLine(int x, int y, int[] line) {
    return GridVisitResult.CONTINUE;
  }

  GridVisitResult visit(int x, int y, int value);

  default GridVisitResult postVisitLine(int x, int y, int[] line) {
    return GridVisitResult.CONTINUE;
  }
}
