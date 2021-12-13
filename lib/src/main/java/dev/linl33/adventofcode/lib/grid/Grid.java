package dev.linl33.adventofcode.lib.grid;

import dev.linl33.adventofcode.lib.point.Point2D;
import dev.linl33.adventofcode.lib.util.PrintUtil;

import java.util.function.Consumer;

public interface Grid {
  int get(int x, int y);

  default int get(Point2D pt) {
    return get(pt.x(), pt.y());
  }

  void set(int x, int y, int value);

  int width();

  int height();

  int[] row(int y);

  int[] column(int x);

  default GridConfiguration configuration() {
    return GridConfiguration.DEFAULT;
  }

  default boolean isWithinBounds(int x, int y) {
    return x >= 0 && y >= 0 && x < width() && y < height();
  }

  default boolean isWithinBounds(Point2D pt) {
    return isWithinBounds(pt.x(), pt.y());
  }

  default void print() {
    print(PrintUtil::enhancedPrint);
  }

  default void print(Consumer<String> printTo) {
    visit(new GridVisitor() {
      StringBuilder sb;

      @Override
      public GridVisitResult preVisitLine(int x, int y, int[] line) {
        sb = new StringBuilder();
        return GridVisitResult.CONTINUE;
      }

      @Override
      public GridVisitResult visit(int x, int y, int value) {
        sb.append((char) value == '#' ? PrintUtil.FULL_BLOCK : ' ');
        return GridVisitResult.CONTINUE;
      }

      @Override
      public GridVisitResult postVisitLine(int x, int y, int[] line) {
        printTo.accept(sb.toString());
        return GridVisitResult.CONTINUE;
      }
    });
  }

  default Grid visit(GridVisitor visitor, GridVisitOptions... options) {
    var byRow = true;
    var invertX = false;
    var invertY = false;

    for (GridVisitOptions opt : options) {
      switch (opt) {
        case BY_COLUMN -> byRow = false;
        case INVERT_X -> invertX = true;
        case INVERT_Y -> invertY = true;
      }
    }

    if (invertX || invertY) {
      throw new UnsupportedOperationException();
    }

    var firstAxisLimit = byRow ? height() : width();
    var secondAxisLimit = byRow ? width() : height();

    var secondAxisStart = 0;

    for (int firstAxis = 0; firstAxis < firstAxisLimit; firstAxis++) {
      var line = byRow ? row(firstAxis) : column(firstAxis);
      var preVisitLineResult = visitor.preVisitLine(byRow ? secondAxisStart : firstAxis, byRow ? firstAxis : secondAxisStart, line);

      if (!(preVisitLineResult instanceof GridVisitResult.IContinue)) {
        if (preVisitLineResult instanceof GridVisitResult.ITerminate) {
          return this;
        } else if (preVisitLineResult instanceof GridVisitResult.ISkipLine) {
          continue;
        } else if (preVisitLineResult instanceof GridVisitResult.ISkipToNextLineWith skipToNextLineWith) {
          secondAxisStart = skipToNextLineWith.startWith();
        }
      }

      int secondAxis;
      for (secondAxis = secondAxisStart; secondAxis < secondAxisLimit; secondAxis++) {
        var x = byRow ? secondAxis : firstAxis;
        var y = byRow ? firstAxis : secondAxis;

        var result = visitor.visit(x, y, get(x, y));

        if (result instanceof GridVisitResult.Mutate m) {
          set(x, y, m.newValue());
        }

        if (result instanceof GridVisitResult.IContinue) {
          continue;
        }

        if (result instanceof GridVisitResult.ITerminate) {
          return this;
        } else if (result instanceof GridVisitResult.ISkipLine) {
          break;
        } else if (result instanceof GridVisitResult.ISkipToNextLineWith skipToNextLineWith) {
          secondAxisStart = skipToNextLineWith.startWith();
          break;
        }
      }

      var postVisitLineResult = visitor.postVisitLine(byRow ? secondAxis : firstAxis, byRow ? firstAxis : secondAxis, line);

      if (postVisitLineResult instanceof GridVisitResult.IContinue) {
        continue;
      }

      if (postVisitLineResult instanceof GridVisitResult.ITerminate) {
        return this;
      } else if (postVisitLineResult instanceof GridVisitResult.ISkipToNextLineWith skipToNextLineWith) {
        secondAxisStart = skipToNextLineWith.startWith();
      }
    }

    return this;
  }
}
