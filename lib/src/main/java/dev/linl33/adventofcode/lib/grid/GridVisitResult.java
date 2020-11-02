package dev.linl33.adventofcode.lib.grid;

public sealed interface GridVisitResult {
  IContinue CONTINUE = new Continue();
  ITerminate TERMINATE = new Terminate();
  ISkipLine SKIP_LINE = new SkipLine();

  sealed interface IContinue extends GridVisitResult permits Continue, Mutate.MutateAndContinue {}

  final class Continue implements IContinue {
    private Continue() {}
  }

  sealed interface ITerminate extends GridVisitResult permits Terminate, Mutate.MutateAndTerminate {}

  final class Terminate implements ITerminate {
    private Terminate() {}
  }

  sealed interface ISkipLine extends GridVisitResult permits SkipLine, Mutate.MutateAndSkipLine {}

  final class SkipLine implements ISkipLine {
    private SkipLine() {}
  }

  sealed interface ISkipToNextLineWith extends GridVisitResult permits SkipToNextLineWith, Mutate.MutateAndSkipToNextLineWith {
    int startWith();
  }

  record SkipToNextLineWith(int startWith) implements ISkipToNextLineWith {}

  sealed interface Mutate extends GridVisitResult {
    int newValue();

    record MutateAndContinue(int newValue) implements Mutate, IContinue {}

    record MutateAndTerminate(int newValue) implements Mutate, ITerminate {}

    record MutateAndSkipLine(int newValue) implements Mutate, ISkipLine {}

    record MutateAndSkipToNextLineWith(int newValue, int startWith) implements Mutate, ISkipToNextLineWith {}
  }
}
