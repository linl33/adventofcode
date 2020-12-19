package dev.linl33.adventofcode.year2020;

import dev.linl33.adventofcode.lib.util.AdventUtil;

import java.io.BufferedReader;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day19 extends AdventSolution2020<Integer, Integer> {
  private static final String GROUP_START = "(?:";
  private static final String GROUP_END = ")";
  private static final String REGEX_OR = "|";
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
            // use a CharBuffer for fast subSequence
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
                                  Function<List<String>[], Predicate<String>> rulesToPredicate) {
    var input = AdventUtil.readInputGrouped(reader).iterator();
    var rules = parseRules(input.next());

    return (int) input.next()
        .filter(rulesToPredicate.apply(rules))
        .count();
  }

  private static List<String>[] parseRules(Stream<String> stream) {
    // use single-character split to avoid regex compilation in split
    // keep track of the max because the IDs in example input for part 2 are discontinuous

    @SuppressWarnings("unchecked")
    var output = (List<String>[]) new List[1 << 8];
    var aMax = new AtomicInteger(-1);

    stream.forEach(line -> {
      var sep = line.indexOf(':');
      var id = Integer.parseInt(line, 0, sep, 10);

      var orIdx = line.indexOf('|');
      if (orIdx < 0) {
        output[id] = List.of(line.substring(sep + 2));
      } else {
        output[id] = List.of(
            line.substring(sep + 2, orIdx - 1),
            line.substring(orIdx + 2)
        );
      }

      if (id > aMax.getPlain()) {
        aMax.setPlain(id);
      }
    });

    return Arrays.copyOf(output, aMax.getPlain() + 1);
  }

  private static String makeRegex(List<String> rule, IntFunction<String> resolveRule) {
    if (rule.size() > 1) {
      // or always joins exactly 2 clauses
      var left = makeRegex(rule.get(0), resolveRule);
      var right = makeRegex(rule.get(1), resolveRule);

      if (left.charAt(0) == right.charAt(0) && (left.charAt(0) == CHAR_A || left.charAt(0) == CHAR_B)) {
        return left.charAt(0) + GROUP_START + left.substring(1) + REGEX_OR + right.substring(1) + GROUP_END;
      } else {
        return GROUP_START + left + REGEX_OR + right + GROUP_END;
      }
    }

    return makeRegex(rule.get(0), resolveRule);
  }

  private static String makeRegex(String oneRule, IntFunction<String> resolveRule) {
    if (oneRule.length() > 1 && (oneRule.charAt(1) == CHAR_A || oneRule.charAt(1) == CHAR_B)) {
      return oneRule.charAt(1) == CHAR_A ? STR_A : STR_B;
    } else {
      return Arrays
          .stream(oneRule.split(" "))
          .mapToInt(Integer::parseInt)
          .mapToObj(resolveRule)
          .collect(Collectors.joining());
    }
  }

  private static class RuleResolver implements IntFunction<String> {
    private final List<String>[] rules;
    private final String[] cache;

    private RuleResolver(List<String>[] rules) {
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
