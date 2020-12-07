package dev.linl33.adventofcode.lib.graph.intgraph;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

public record IdLayout<T>(int length,
                          int allocationSize,
                          @NotNull List<LayoutElement<T>> elements) {
  public static record LayoutElement<T>(int bits,
                                        int maxId,
                                        @NotNull ToIntFunction<T> objIntAssignment) {
    public LayoutElement {
      if (bits < 0 || bits > (Integer.SIZE - 2)) {
        throw new IllegalArgumentException("Invalid bits " + bits);
      }

      if (maxId > (1 << bits)) {
        throw new IllegalArgumentException("Invalid maxId " + maxId);
      }
    }
  }

  public IdLayout(@NotNull List<LayoutElement<T>> elements) {
    this(
        elements
            .stream()
            .mapToInt(LayoutElement::bits)
            .sum(),
        calculateAllocationSize(elements),
        elements
    );
  }

  public IdLayout {
    Objects.requireNonNull(elements);

    if (length < elements.size() || allocationSize < (1 << (elements.size() - 1)) + 1) {
      throw new IllegalArgumentException();
    }

    elements = List.copyOf(elements);
  }

  private static <T> int calculateAllocationSize(@NotNull List<LayoutElement<T>> elements) {
    if (elements.isEmpty()) {
      return 0;
    }

    var size = elements.get(0).maxId;

    for (int i = 1; i < elements.size(); i++) {
      size *= (1 << elements.get(i).bits);
    }

    return size;
  }
}
