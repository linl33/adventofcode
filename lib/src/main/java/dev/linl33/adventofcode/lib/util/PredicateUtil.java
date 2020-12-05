package dev.linl33.adventofcode.lib.util;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class PredicateUtil {
  public static <T> Predicate<T> truePredicate() {
    return t -> true;
  }

  public static <T> Predicate<T> falsePredicate() {
    return t -> false;
  }

  public static <T, U> BiPredicate<T, U> trueBiPredicate() {
    return (t, u) -> true;
  }

  public static <T, U> BiPredicate<T, U> falseBiPredicate() {
    return (t, u) -> false;
  }
}
