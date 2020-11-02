package dev.linl33.adventofcode.year2019;

import dev.linl33.adventofcode.lib.grid.*;
import dev.linl33.adventofcode.year2019.intcodevm.IntcodeVM;

import java.io.BufferedReader;
import java.util.concurrent.atomic.AtomicInteger;

public class Day19 extends AdventSolution2019<Integer, Integer> {
  public static void main(String[] args) {
    new Day19().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    var sum = new AtomicInteger(0);
    readTractorBeam(new IntcodeVM(reader), 50, (GridValueConsumer) sum::addAndGet);

    return sum.intValue();
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    var colHeight = new int[1200];
    var result = new AtomicInteger(0);

    readTractorBeam(new IntcodeVM(reader), 1200, new GridVisitor() {
      int counter = 0;

      @Override
      public GridVisitResult preVisitLine(int x, int y, int[] line) {
        counter = 0;

        return GridVisitResult.CONTINUE;
      }

      @Override
      public GridVisitResult visit(int x, int y, int value) {
        colHeight[x]++;

        if (colHeight[x] >= 100) {
          counter++;
        }

        if (counter == 100) {
          result.set((x - 99) * 10000 + (y - 99));

          return GridVisitResult.TERMINATE;
        }

        return GridVisitResult.CONTINUE;
      }
    });

    int resultInt = result.intValue();

    if (resultInt == 0) {
      throw new IllegalArgumentException();
    }

    return resultInt;
  }

  private static void readTractorBeam(IntcodeVM tractorBeamVm, int dim, GridVisitor visitor) {
    new TractorBeamGrid(tractorBeamVm, dim)
        .visit(new TractorBeamVisitor(visitor), GridVisitOptions.BY_ROW);
  }

  private static class TractorBeamVisitor implements GridVisitor {
    private final GridVisitor after;
    private boolean found;
    private int start;

    public TractorBeamVisitor(GridVisitor after) {
      this.after = after;
      found = false;
      start = 0;
    }

    @Override
    public GridVisitResult preVisitLine(int x, int y, int[] line) {
      found = false;

      return after.preVisitLine(x, y, line);
    }

    @Override
    public GridVisitResult visit(int x, int y, int value) {
      if (value == 1) {
        var afterResult = after.visit(x, y, value);

        if (afterResult instanceof GridVisitResult.ITerminate) {
          return afterResult;
        }
      }

      if (!found && value == 1) {
        found = true;
        start = x;
        return GridVisitResult.CONTINUE;
      }

      if (found && value == 0) {
        return new GridVisitResult.SkipToNextLineWith(start);
      }

      return GridVisitResult.CONTINUE;
    }

    @Override
    public GridVisitResult postVisitLine(int x, int y, int[] line) {
      return after.postVisitLine(x, y, line);
    }
  }

  private static class TractorBeamGrid implements Grid {
    private final IntcodeVM vm;
    private final int dim;

    public TractorBeamGrid(IntcodeVM vm, int dim) {
      this.vm = vm;
      this.dim = dim;
    }

    @Override
    public int get(int x, int y) {
      vm.getInput().add((long) x);
      vm.getInput().add((long) y);

      vm.executeNonBlocking(IntcodeVM.ExecMode.STATELESS);

      return vm.getOutput().remove().intValue();
    }

    @Override
    public void set(int x, int y, int value) {
      // ignore
    }

    @Override
    public int width() {
      return dim;
    }

    @Override
    public int height() {
      return dim;
    }

    @Override
    public int[] row(int y) {
      return null;
    }

    @Override
    public int[] column(int x) {
      return null;
    }
  }
}
