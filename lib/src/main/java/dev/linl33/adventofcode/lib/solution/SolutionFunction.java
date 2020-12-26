package dev.linl33.adventofcode.lib.solution;

import java.util.function.BiFunction;

@FunctionalInterface
public interface SolutionFunction<T> extends BiFunction<AdventSolution<?, ?>, ResourceIdentifier, T> {
}
