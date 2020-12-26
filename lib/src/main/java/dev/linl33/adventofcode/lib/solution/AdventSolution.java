package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.util.internal.LoggingUtil;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Locale;

public interface AdventSolution<T1, T2> {
  T1 part1(ResourceIdentifier identifier) throws Exception;

  T2 part2(ResourceIdentifier identifier) throws Exception;

  Logger getLogger();

  int getYear();

  int getDay();

  default void runAndPrintAll() {
    getLogger().info(
        LoggingUtil.SOLUTION_MARKER,
        "Year {} Day {}",
        this::getYear,
        () -> Integer.toString(getDay()).transform(s -> s.length() < 2 ? "0" + s : s)
    );

    Arrays
        .stream(getSolutionParts())
        .peek(p -> getLogger().info(LoggingUtil.SOLUTION_MARKER, "{} {}", getClass().getSimpleName(), p.name()))
        .forEachOrdered(this::print);
  }

  <U, A extends AdventSolution<T1, T2>> U run(ThrowingBiFunction<A, ?, U> customPart, ResourceIdentifier resource);

  <U, A extends AdventSolution<T1, T2>> void print(ThrowingBiFunction<A, ?, U> customPart,
                                                   ThrowingBiFunction<A, U, ?> printMapping,
                                                   ResourceIdentifier resource);

  default Object run(SolutionPart solutionPart, ResourceIdentifier resource) {
    return solutionPart.part.apply(this, resource);
  }

  default Object run(SolutionPart solutionPart) {
    return solutionPart.part.apply(this, solutionPart.defaultResource.apply(this));
  }

  default void print(SolutionPart solutionPart, ResourceIdentifier resource) {
    solutionPart.printer.accept(this, resource);
  }

  default void print(SolutionPart solutionPart) {
    solutionPart.defaultResource.andThenConsume(solutionPart.printer).accept(this);
  }

  default Object part1PrintMapping(T1 part1Result) throws Exception {
    return part1Result;
  }

  default Object part2PrintMapping(T2 part2Result) throws Exception {
    return part2Result;
  }

  @SuppressWarnings("unchecked")
  default Object part1PrintMappingInternal(Object part1Result) throws Exception {
    return part1PrintMapping((T1) part1Result);
  }

  @SuppressWarnings("unchecked")
  default Object part2PrintMappingInternal(Object part2Result) throws Exception {
    return part2PrintMapping((T2) part2Result);
  }

  default ResourceIdentifier getDefaultResource() {
    return new ClasspathResourceIdentifier(getClass().getSimpleName().toLowerCase(Locale.ROOT));
  }

  default ResourceIdentifier getPart1Resource() {
    return getDefaultResource();
  }

  default ResourceIdentifier getPart2Resource() {
    return getDefaultResource();
  }

  default SolutionPart[] getSolutionParts() {
    return getDay() != 25 ? SolutionPart.values() : new SolutionPart[] {SolutionPart.PART_1};
  }
}
