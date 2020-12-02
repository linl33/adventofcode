package dev.linl33.adventofcode.year2020;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.function.Predicate;

public class Day2 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    new Day2().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return countValidPasswords(reader, Day2::validatePart1);
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return countValidPasswords(reader, Day2::validatePart2);
  }

  private static int countValidPasswords(@NotNull BufferedReader reader,
                                         @NotNull Predicate<PasswordValidationEntry> validationRule) {
    return (int) reader
        .lines()
        .map(PasswordValidationEntry::parse)
        .filter(validationRule)
        .count();
  }

  private static boolean validatePart1(@NotNull PasswordValidationEntry v) {
    var count = v.password().chars().filter(c -> c == v.constraint()).count();
    return count >= v.low() && count <= v.high();
  }

  private static boolean validatePart2(@NotNull PasswordValidationEntry v) {
    return (v.password().charAt(v.low() - 1) == v.constraint())
        != (v.password().charAt(v.high() - 1) == v.constraint());
  }

  private static record PasswordValidationEntry(@NotNull String password,
                                                char constraint,
                                                int low,
                                                int high) {
    public static PasswordValidationEntry parse(@NotNull String entry) {
      var parts = entry.split(" ");
      var bounds = parts[0].split("-");

      return new PasswordValidationEntry(
          parts[2],
          parts[1].charAt(0),
          Integer.parseInt(bounds[0]),
          Integer.parseInt(bounds[1])
      );
    }
  }
}
