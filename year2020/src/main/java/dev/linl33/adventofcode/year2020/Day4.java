package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.util.AdventUtil;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day4 extends AdventSolution2020<Integer, Integer> {
  public static void main(String[] args) {
    new Day4().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) {
    return (int) readCredentialEntries(reader)
        .filter(Passport.VALIDATE_PART_1)
        .count();
  }

  @Override
  public Integer part2(BufferedReader reader) {
    return (int) readCredentialEntries(reader)
        .filter(Passport.VALIDATE_PART_2)
        .count();
  }

  private static Stream<Passport> readCredentialEntries(BufferedReader reader) {
    return AdventUtil
        .readInputAsGroups(reader)
        .map(stream -> stream.flatMap(str -> Arrays.stream(str.split(" "))))
        .map(stream -> stream
            .map(str -> str.split(":"))
            .collect(Collectors.toUnmodifiableMap(
                arr -> arr[0],
                arr -> arr[1]
            ))
        )
        .map(Passport::parse);
  }

  private static record Passport(@Nullable String byr,
                                 @Nullable String iyr,
                                 @Nullable String eyr,
                                 @Nullable String hgt,
                                 @Nullable String hcl,
                                 @Nullable String ecl,
                                 @Nullable String pid,
                                 @Nullable String cid) {
    public static final Predicate<Passport> VALIDATE_PART_1 = Passport::requiredFieldsPresent;
    public static final Predicate<Passport> VALIDATE_PART_2 = VALIDATE_PART_1
        .and(Passport::validateByr)
        .and(Passport::validateIyr)
        .and(Passport::validateEyr)
        .and(Passport::validateHgt)
        .and(Passport::validateHcl)
        .and(Passport::validateEcl)
        .and(Passport::validatePid);

    private static final Set<String> VALID_ECL = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    public static Passport parse(Map<String, String> fields) {
      try {
        // TODO: declare constructor as static field
        //       doesn't work because of JDK-8243057: wait for JDK16

        return (Passport) Passport.class
            .getDeclaredConstructors()[0]
            .newInstance(Arrays
                .stream(Passport.class.getRecordComponents())
                .map(RecordComponent::getName)
                .map(fields::get)
                .toArray()
            );
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    public boolean requiredFieldsPresent() {
      return byr != null &&
          iyr != null &&
          eyr != null &&
          hgt != null &&
          hcl != null &&
          ecl != null &&
          pid != null;
    }

    public boolean validateByr() {
      return validateYearInRange(byr, 1920, 2002);
    }

    public boolean validateIyr() {
      return validateYearInRange(iyr, 2010, 2020);
    }

    public boolean validateEyr() {
      return validateYearInRange(eyr, 2020, 2030);
    }

    public boolean validateHgt() {
      if (hgt == null) {
        return false;
      }

      int low;
      int high;

      if (hgt.endsWith("cm")) {
        low = 150;
        high = 193;
      } else if (hgt.endsWith("in")) {
        low = 59;
        high = 76;
      } else {
        return false;
      }

      return validateIntInRange(Integer.parseInt(hgt, 0, hgt.length() - 2, 10), low, high);
    }

    public boolean validateHcl() {
      return hcl != null &&
          hcl.length() == 7 &&
          hcl
              .chars()
              .skip(1)
              .allMatch(c -> Character.isDigit(c) || (c >= 'a' && c <= 'f'));
    }

    public boolean validateEcl() {
      return ecl != null &&
          ecl.length() == 3 &&
          VALID_ECL.contains(ecl);
    }

    public boolean validatePid() {
      return pid != null &&
          pid.length() == 9 &&
          pid.chars().allMatch(Character::isDigit);
    }

    private static boolean validateYearInRange(String val, int start, int end) {
      return validateIntInRange(Integer.parseInt(val), start, end);
    }

    private static boolean validateIntInRange(int i, int start, int end) {
      return i >= start && i <= end;
    }
  }
}
