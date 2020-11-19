package dev.linl33.adventofcode.lib.function;

@FunctionalInterface
public interface BiIntConsumer {
  public static final BiIntConsumer IDENTITY = (a, b) -> {};

  void accept(int first, int second);

  default BiIntConsumer andThen(BiIntConsumer after) {
    return (first, second) -> {
      accept(first, second);
      after.accept(first, second);
    };
  }
}
