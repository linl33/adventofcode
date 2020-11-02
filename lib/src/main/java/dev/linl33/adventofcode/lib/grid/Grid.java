package dev.linl33.adventofcode.lib.grid;

public interface Grid {
  int get(int x, int y);

  void set(int x, int y, int value);

  int width();

  int height();

  int[] row(int y);

  int[] column(int x);

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
