package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.ByteBufferAsCharSequence;
import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 extends AdventSolution2024<Integer, Integer> implements ByteBufferAdventSolution<Integer, Integer>, NullBufferedReaderSolution<Integer, Integer> {
  public static void main() {
    new Day4().runAndPrintAll();
  }

  @Override
  public Integer part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part1(identifier);
  }

  @Override
  public Integer part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return ByteBufferAdventSolution.super.part2(identifier);
  }

  @Override
  public Integer part1(@NotNull ByteBuffer byteBuffer) {
    var lines = new ByteBufferAsCharSequence(byteBuffer);

    var patterns = new ArrayList<Pattern>();
    patterns.add(Pattern.compile("(X)MAS", Pattern.MULTILINE));
    patterns.add(Pattern.compile("(S)AMX", Pattern.MULTILINE));

    var squareDim = ((int) Math.sqrt(4 * byteBuffer.limit() + 1) - 1) / 2;
    for (var i = 0; i < squareDim; i++) {
      patterns.add(Pattern.compile("^[A-Z]{" + i + "}(X).*\\n" + "^[A-Z]{" + i + "}M.*\\n" + "^[A-Z]{" + i + "}A.*\\n" + "^[A-Z]{" + i + "}S.*", Pattern.MULTILINE));
      patterns.add(Pattern.compile("^[A-Z]{" + i + "}(S).*\\n" + "^[A-Z]{" + i + "}A.*\\n" + "^[A-Z]{" + i + "}M.*\\n" + "^[A-Z]{" + i + "}X.*", Pattern.MULTILINE));

      patterns.add(Pattern.compile("^[A-Z]{" + i + "}(X).*\\n" + "^[A-Z]{" + (i + 1) + "}M.*\\n" + "^[A-Z]{" + (i + 2) + "}A.*\\n" + "^[A-Z]{" + (i + 3) + "}S.*", Pattern.MULTILINE));
      patterns.add(Pattern.compile("^[A-Z]{" + i + "}(S).*\\n" + "^[A-Z]{" + (i + 1) + "}A.*\\n" + "^[A-Z]{" + (i + 2) + "}M.*\\n" + "^[A-Z]{" + (i + 3) + "}X.*", Pattern.MULTILINE));

      patterns.add(Pattern.compile("^[A-Z]{" + (i + 3) + "}(X).*\\n" + "^[A-Z]{" + (i + 2) + "}M.*\\n" + "^[A-Z]{" + (i + 1) + "}A.*\\n" + "^[A-Z]{" + i + "}S.*", Pattern.MULTILINE));
      patterns.add(Pattern.compile("^[A-Z]{" + (i + 3) + "}(S).*\\n" + "^[A-Z]{" + (i + 2) + "}A.*\\n" + "^[A-Z]{" + (i + 1) + "}M.*\\n" + "^[A-Z]{" + i + "}X.*", Pattern.MULTILINE));
    }

    return patterns
      .stream()
      .map(pattern -> pattern.matcher(lines))
      .filter(Matcher::find)
      .mapToInt(matcher -> {
        var count = 0;
        do {
          count++;
        } while (matcher.find(matcher.start(1) + 1));
        return count;
      })
      .sum();
  }

  @Override
  public Integer part2(@NotNull ByteBuffer byteBuffer) {
    var lines = new ByteBufferAsCharSequence(byteBuffer);

    var patterns = new ArrayList<Pattern>();

    var squareDim = ((int) Math.sqrt(4 * byteBuffer.limit() + 1) - 1) / 2;
    for (var i = 0; i < squareDim - 2; i++) {
      patterns.add(Pattern.compile("^[A-Z]{" + i + "}M(.)M.*\\n" + "^[A-Z]{" + (i + 1) + "}A.+\\n" + "^[A-Z]{" + i + "}S.S.*", Pattern.MULTILINE));
      patterns.add(Pattern.compile("^[A-Z]{" + i + "}M(.)S.*\\n" + "^[A-Z]{" + (i + 1) + "}A.+\\n" + "^[A-Z]{" + i + "}M.S.*", Pattern.MULTILINE));
      patterns.add(Pattern.compile("^[A-Z]{" + i + "}S(.)S.*\\n" + "^[A-Z]{" + (i + 1) + "}A.+\\n" + "^[A-Z]{" + i + "}M.M.*", Pattern.MULTILINE));
      patterns.add(Pattern.compile("^[A-Z]{" + i + "}S(.)M.*\\n" + "^[A-Z]{" + (i + 1) + "}A.+\\n" + "^[A-Z]{" + i + "}S.M.*", Pattern.MULTILINE));
    }

    return patterns
      .stream()
      .map(pattern -> pattern.matcher(lines))
      .filter(Matcher::find)
      .mapToInt(matcher -> {
        var count = 0;
        do {
          count++;
        } while (matcher.find(matcher.start(1) + 1));
        return count;
      })
      .sum();
  }
}
