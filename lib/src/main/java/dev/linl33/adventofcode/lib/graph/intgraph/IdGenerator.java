package dev.linl33.adventofcode.lib.graph.intgraph;

import java.util.function.ToIntFunction;

@FunctionalInterface
public interface IdGenerator<T> extends ToIntFunction<T> {
  static <T> IdGenerator<T> asIdGenerator(IdLayout<T> layout) {
    return (T nodeData) -> {
      int id = 0;

      for (int i = 0; i < layout.elements().size(); i++) {
        var element = layout.elements().get(i);

        var fieldIntValue = element.objIntAssignment().applyAsInt(nodeData);

        if (i == 0) {
          id = fieldIntValue;
        } else {
          id = (id << element.bits()) | fieldIntValue;
        }
      }

      return id;
    };
  }

  int generateId(T object);

  @Override
  default int applyAsInt(T value) {
    return generateId(value);
  }
}
