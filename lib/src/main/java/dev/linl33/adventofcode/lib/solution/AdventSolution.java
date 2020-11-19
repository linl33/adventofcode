package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.UncheckedBiFunction;
import dev.linl33.adventofcode.lib.util.LoggingUtil;
import dev.linl33.adventofcode.lib.util.ResourceUtil;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Locale;

public interface AdventSolution<T1, T2> {
  T1 part1(BufferedReader reader) throws Exception;

  T2 part2(BufferedReader reader) throws Exception;

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

  default <U, V extends AdventSolution<T1, T2>> U run(UncheckedBiFunction<V, BufferedReader, U> customPart,
                                                      String resource) {
    return SolutionUtil.adaptPartToRunGeneric((V) this, customPart, resource);
  }

  default Object run(SolutionPart solutionPart, String resource) {
    return solutionPart.part.apply(this, resource);
  }

  default Object run(SolutionPart solutionPart) {
    return solutionPart.part.apply(this, solutionPart.defaultResource.apply(this));
  }

  default <U, V extends AdventSolution<T1, T2>> void print(UncheckedBiFunction<V, BufferedReader, U> customPart,
                                                           UncheckedBiFunction<V, U, ?> printMapping,
                                                           String resource) {
    SolutionUtil.adaptPartToPrintGeneric((V) this, customPart, printMapping, resource);
  }

  default void print(SolutionPart solutionPart, String resource) {
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

  default String getDefaultResource() {
    return getClass().getSimpleName().toLowerCase(Locale.ROOT);
  }

  default String getPart1Resource() {
    return getDefaultResource();
  }

  default String getPart2Resource() {
    return getDefaultResource();
  }

  default SolutionPart[] getSolutionParts() {
    return getDay() != 25 ? SolutionPart.values() : new SolutionPart[] {SolutionPart.PART_1};
  }

  default BufferedReader resourceSupplier(String resource) {
    return ResourceUtil.readResource(getClass(), resource);
  }
}
