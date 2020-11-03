package dev.linl33.adventofcode.lib.grid;

public interface GridConsumer extends GridVisitor {
  void consume (int x, int y, int value);

  @Override
  default GridVisitResult visit(int x, int y, int value) {
    consume(x, y, value);
    return GridVisitResult.CONTINUE;
  }
}
