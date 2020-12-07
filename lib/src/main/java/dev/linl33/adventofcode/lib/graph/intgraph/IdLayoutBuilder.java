package dev.linl33.adventofcode.lib.graph.intgraph;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class IdLayoutBuilder<T> {
  private final LinkedHashMap<String, Function<T, ?>> fields;
  private final HashMap<String, Map<?, Integer>> intAssignments;

  public IdLayoutBuilder() {
    this.fields = new LinkedHashMap<>();
    this.intAssignments = new HashMap<>();
  }

  @NotNull
  public <V> IdLayoutBuilder<T> addField(@NotNull String name, @NotNull Function<T, V> field) {
    fields.put(name, field);
    return this;
  }

  @NotNull
  public <V> IdLayoutBuilder<T> addField(@NotNull Function<T, V> field) {
    return addField(field.toString(), field);
  }

  @NotNull
  public <V> IdLayoutBuilder<T> addField(@NotNull String name,
                                         @NotNull Function<T, V> field,
                                         @NotNull Map<V, Integer> intAssignment) {
    fields.put(name, field);
    intAssignments.put(name, intAssignment);
    return this;
  }

  @NotNull
  public <R extends Record> IdLayoutBuilder<T> addFieldsFromRecord(@NotNull Class<R> rClass) {
    Arrays
        .stream(rClass.getRecordComponents())
        .forEach(comp -> fields.put(
            comp.toString(),
            (T t) -> {
              try {
                return comp.getAccessor().invoke(t);
              } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
              }
            }
        ));

    return this;
  }

  @NotNull
  public IdLayout<T> build(@NotNull Collection<T> nodes) {
    populateDefaultIntAssignment(nodes);

    return build();
  }

  @NotNull
  public IdLayout<T> build() {
    if (!fields.keySet().equals(intAssignments.keySet())) {
      throw new IllegalArgumentException("Fields are incomplete");
    }

    return fields
        .entrySet()
        .stream()
        .map(entry -> {
          var assignmentForField = intAssignments.get(entry.getKey());

          if (assignmentForField.isEmpty()) {
            return null;
          }

          return new IdLayout.LayoutElement<>(
              sizeInBits(assignmentForField),
              assignmentForField.size(),
              objIntAssignmentFactory(entry.getValue(), assignmentForField)
          );
        })
        .filter(Objects::nonNull)
        .collect(Collectors.collectingAndThen(Collectors.toList(), IdLayout::new));
  }

  private void populateDefaultIntAssignment(@NotNull Collection<T> nodes) {
    fields.forEach((k, accessor) -> intAssignments.computeIfAbsent(
        k,
        __ -> {
          var fieldIMap = new HashMap<Object, Integer>();

          for (T nodeData : nodes) {
            fieldIMap.putIfAbsent(accessor.apply(nodeData), fieldIMap.size());
          }

          return fieldIMap;
        }
    ));
  }

  private static int sizeInBits(@NotNull Map<?, Integer> intAssignment) {
    return Integer.SIZE - Integer.numberOfLeadingZeros(intAssignment.size() - 1);
  }

  @NotNull
  private static <T> ToIntFunction<T> objIntAssignmentFactory(@NotNull Function<T, ?> accessor,
                                                              @NotNull Map<?, Integer> assignment) {
    return (T t) -> accessor.andThen(assignment::get).apply(t);
  }
}
