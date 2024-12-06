package dev.linl33.adventofcode.year2024;

import dev.linl33.adventofcode.lib.ByteBufferAsCharSequence;
import dev.linl33.adventofcode.lib.solution.ByteBufferAdventSolution;
import dev.linl33.adventofcode.lib.solution.NullBufferedReaderSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Day3 extends AdventSolution2024<Integer, Integer> implements ByteBufferAdventSolution<Integer, Integer>, NullBufferedReaderSolution<Integer, Integer> {
  private static final Pattern MUL_INSTR_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)", Pattern.MULTILINE);
  private static final Pattern COND_MUL_INSTR_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");

  public static void main() {
    new Day3().runAndPrintAll();
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
    var memory = new ByteBufferAsCharSequence(byteBuffer);
    return MUL_INSTR_PATTERN
      .matcher(memory)
      .results()
      .mapToInt(result -> evalMul(memory, result))
      .sum();
  }

  @Override
  public Integer part2(@NotNull ByteBuffer byteBuffer) {
    var memory = new ByteBufferAsCharSequence(byteBuffer);
    var results = COND_MUL_INSTR_PATTERN.matcher(memory).results().toList();

    var sum = 0;
    var enabled = true;
    for (var result : results) {
      var instruction = memory.subSequence(result.start(), result.end());
      if (CharSequence.compare(instruction, "do()") == 0) {
        enabled = true;
      } else if (CharSequence.compare(instruction, "don't()") == 0) {
        enabled = false;
      } else if (enabled) {
        sum += evalMul(memory, result);
      }
    }

    return sum;
  }

  private static int evalMul(CharSequence memory, MatchResult result) {
    var left = Integer.parseInt(memory, result.start(1), result.end(1), 10);
    var right = Integer.parseInt(memory, result.start(2), result.end(2), 10);
    return left * right;
  }
}
