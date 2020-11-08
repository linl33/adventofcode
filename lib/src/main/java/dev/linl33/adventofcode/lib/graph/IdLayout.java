package dev.linl33.adventofcode.lib.graph;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public record IdLayout<T>(int length,
                          int allocationSize,
                          @NotNull List<LayoutElement<T>> elements) {
  public static record LayoutElement<T>(int length, int maxId, @NotNull Function<T, ?> accessor) {
    public LayoutElement {
      if (length < 0 || length > (Integer.SIZE - 2)) {
        throw new IllegalArgumentException("invalid length");
      }

      if (maxId > (1 << length)) {
        throw new IllegalArgumentException("Invalid maxId");
      }
    }
  }

  public IdLayout(@NotNull List<LayoutElement<T>> elements) {
    this(elements.stream().mapToInt(LayoutElement::length).sum(), calculateAllocationSize(elements), elements);
  }

  public IdLayout {
    Objects.requireNonNull(elements);

    if (length < elements.size() || allocationSize < (1 << (elements.size() - 1)) + 1 || elements.isEmpty()) {
      throw new IllegalArgumentException();
    }

    elements = List.copyOf(elements);
  }

  private static <T> int calculateAllocationSize(@NotNull List<LayoutElement<T>> elements) {
    var size = elements.get(0).maxId;

    for (int i = 1; i < elements.size(); i++) {
      size *= (1 << elements.get(i).length);
    }

    return size;
  }
}
