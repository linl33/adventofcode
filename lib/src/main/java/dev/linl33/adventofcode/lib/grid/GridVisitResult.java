package dev.linl33.adventofcode.lib.grid;

public sealed interface GridVisitResult {
  IContinue CONTINUE = new Continue();
  ITerminate TERMINATE = new Terminate();
  ISkipLine SKIP_LINE = new SkipLine();

  sealed interface IContinue extends GridVisitResult {}

  final class Continue implements IContinue {
    private Continue() {}
  }

  sealed interface ITerminate extends GridVisitResult {}

  final class Terminate implements ITerminate {
    private Terminate() {}
  }

  sealed interface ISkipLine extends GridVisitResult {}

  final class SkipLine implements ISkipLine {
    private SkipLine() {}
  }

  sealed interface ISkipToNextLineWith extends GridVisitResult {
    int startWith();
  }

  final record SkipToNextLineWith(int startWith) implements ISkipToNextLineWith {}

  sealed interface Mutate extends GridVisitResult {
    int newValue();

    final record MutateAndContinue(int newValue) implements Mutate, IContinue {}

    final record MutateAndTerminate(int newValue) implements Mutate, ITerminate {}

    final record MutateAndSkipLine(int newValue) implements Mutate, ISkipLine {}

    final record MutateAndSkipToNextLineWith(int newValue, int startWith) implements Mutate, ISkipToNextLineWith {}
  }
}
