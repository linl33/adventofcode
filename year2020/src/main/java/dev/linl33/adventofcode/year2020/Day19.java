package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.util.AdventUtil;

import java.io.BufferedReader;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day19 extends AdventSolution2020<Integer, Integer> {
  private static final String GROUP_START = "(?:";
  private static final String GROUP_END = ")";
  private static final String REGEX_OR = "|";
  private static final String REGEX_ANY = ".";
  private static final char CHAR_A = 'a';
  private static final char CHAR_B = 'b';
  private static final String STR_A = "a";
  private static final String STR_B = "b";

  public static void main(String[] args) {
    new Day19().runAndPrintAll();
  }

  @Override
  public Integer part1(BufferedReader reader) throws Exception {
    return countMatches(
        reader,
        rules -> Pattern
            .compile(makeRegex(rules[0], new RuleResolver(rules)))
            .asMatchPredicate()
    );
  }

  @Override
  public Integer part2(BufferedReader reader) throws Exception {
    return countMatches(
        reader,
        rules -> {
          // the pattern for part 2 reduces into rule42{x}rule31{y} where x > y
          // in other words the pattern defined by rule 42 must repeat x times
          // and the pattern defined by rule 31 must repeat y time
          // such that {x, y | x > 0; y > 0; x > y}

          var resolver = new RuleResolver(rules);
          var regex42 = Pattern.compile("^" + makeRegex(rules[42], resolver));
          var regex31 = Pattern.compile("^" + makeRegex(rules[31], resolver));

          return line -> {
            // use CharBuffer for fast subSequence
            // have to subSequence because Matcher.find(int start) doesn't respect start of line anchor '^'
            var cb = CharBuffer.wrap(line);

            var counter42 = 0;
            var matcher = regex42.matcher(cb);

            while (matcher.find()) {
              counter42++;
              cb = cb.subSequence(matcher.end(), cb.remaining());
              matcher.reset(cb);
            }

            // since the difference between the 2 parts need to be >= 1
            // 2 is the minimum amount required
            if (counter42 < 2 || cb.isEmpty()) {
              return false;
            }

            matcher = regex31.matcher(cb);

            // rule 31 has (counter42 - 1) rounds to match fully
            while (counter42 > 1 && matcher.find()) {
              cb = cb.subSequence(matcher.end(), cb.remaining());
              matcher.reset(cb);
              counter42--;
            }

            // check that this line has been matched fully
            return cb.isEmpty();
          };
        }
    );
  }

  private static int countMatches(BufferedReader reader,
                                  Function<int[], Predicate<String>> rulesToPredicate) {
    var input = AdventUtil.readInputGrouped(reader).iterator();
    var rules = parseRules(input.next());

    return (int) input.next()
        .filter(rulesToPredicate.apply(rules))
        .count();
  }

  private static int[] parseRules(Stream<String> stream) {
    var output = new int[1 << 8];
    var max = new AtomicInteger(-1);

    stream.forEach(line -> {
      var sep = line.indexOf(':');
      var id = Integer.parseInt(line, 0, sep, 10);

      var rule = 0;
      var mul = 1;
      var next = 0;
      for (int i = line.length() - 1; i >= sep + 1; i--) {
        var c = line.charAt(i);

        if (c == '"') {
          rule = line.charAt(i - 1);
          break;
        }

        switch (c) {
          case ' ' -> {
            rule <<= 8;
            rule |= next;
            mul = 1;
            next = 0;
          }
          case '|' -> {
            // handle the case where the right hand side of an or clause has only 1 term

            // make sure that term ends up at the left most position
            // it has to be the left most position because
            // the makeRegex method uses the value of the left most 8 bits
            // to infer if a rule contains an or clause
            if ((rule & 0xFF00) == 0) {
              rule <<= 16;
            }

            i--;
          }
          default -> {
            next += mul * (c - '0');
            mul *= 10;
          }
        }
      }

      output[id] = rule;
      if (id > max.getPlain()) {
        max.setPlain(id);
      }
    });

    return Arrays.copyOf(output, max.getPlain() + 1);
  }

  private static String makeRegex(int ruleSet, IntFunction<String> resolveRule) {
    // if the left most 8 bits are set then this is an or clause
    if ((ruleSet & 0xFF000000) != 0) {
      // or always joins exactly 2 clauses
      var left = makeRegexSingleRule(ruleSet & 0xFFFF, resolveRule);
      var right = makeRegexSingleRule((ruleSet & 0xFFFF0000) >>> 16, resolveRule);

      if (left.charAt(0) == right.charAt(0) && (left.charAt(0) == CHAR_A || left.charAt(0) == CHAR_B)) {
        if (left.length() == right.length() && left.length() == 2) {
          // this clause is prefix(?:a|b) or prefix(?:b|a)
          // which is equivalent to matching prefix + any character
          return left.charAt(0) + REGEX_ANY;
        } else {
          return left.charAt(0) + GROUP_START + left.substring(1) + REGEX_OR + right.substring(1) + GROUP_END;
        }
      } else {
        if (left.length() == 1 && right.length() == 1) {
          // this clause is (?:a|b) or (?:b|a)
          // which is equivalent to matching any character
          return REGEX_ANY;
        } else {
          return GROUP_START + left + REGEX_OR + right + GROUP_END;
        }
      }
    }

    return makeRegexSingleRule(ruleSet, resolveRule);
  }

  private static String makeRegexSingleRule(int oneRule, IntFunction<String> resolveRule) {
    if (oneRule == CHAR_A || oneRule == CHAR_B) {
      return oneRule == CHAR_A ? STR_A : STR_B;
    } else {
      // most rules have 2 terms
      // the example for part 1 has a rule with 3 terms

      var first = oneRule & 0xFF;
      var second = (oneRule & 0xFF00) >>> 8;
      var third = (oneRule & 0xFF0000) >>> 16;

      if (third != 0) {
        return resolveRule.apply(first) + resolveRule.apply(second) + resolveRule.apply(third);
      }

      // if exactly one of them is 0
      if (first == 0 || second == 0) {
        return resolveRule.apply(first | second);
      }

      return resolveRule.apply(first) + resolveRule.apply(second);
    }
  }

  private static class RuleResolver implements IntFunction<String> {
    private final int[] rules;
    private final String[] cache;

    private RuleResolver(int[] rules) {
      this.rules = rules;
      this.cache = new String[rules.length];
    }

    @Override
    public String apply(int value) {
      if (cache[value] == null) {
        cache[value] = makeRegex(rules[value], this);
      }

      return cache[value];
    }
  }
}
