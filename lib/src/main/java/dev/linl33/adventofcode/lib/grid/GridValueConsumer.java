package dev.linl33.adventofcode.lib.grid;

@FunctionalInterface
public interface GridValueConsumer extends GridVisitor {
  void consume(int value);

  @Override
  default GridVisitResult visit(int x, int y, int value) {
    consume(value);

    return GridVisitResult.CONTINUE;
  }
}
