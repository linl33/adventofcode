package dev.linl33.adventofcode.testlib;

import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class TestCaseArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<TestCaseSource> {
  private TestPart[] testParts;
  private ValueSource[] extraArgs;

  @Override
  public void accept(TestCaseSource testCaseSource) {
    this.testParts = testCaseSource.value();
    this.extraArgs = testCaseSource.extraArgs();
  }

  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
    if (!AdventSolutionTest.class.isAssignableFrom(context.getRequiredTestClass())) {
      throw new ExtensionConfigurationException("Test class must implement AdventSolutionTest");
    }

    var instance = (AdventSolutionTest<?, ?>) context
        .getStore(AdventSolutionExtension.NAMESPACE)
        .getOrComputeIfAbsent(context.getRequiredTestClass());

    @SuppressWarnings("unchecked")
    var extraArgIts = (Iterator<Object>[]) Arrays
        .stream(extraArgs)
        .map(valueSource -> Stream
            .of(
                valueSource.shorts(),
                valueSource.bytes(),
                valueSource.ints(),
                valueSource.longs(),
                valueSource.floats(),
                valueSource.doubles(),
                valueSource.chars(),
                valueSource.booleans(),
                valueSource.strings(),
                valueSource.classes()
            )
            .filter(a -> Array.getLength(a) > 0)
            .flatMap(a -> IntStream.range(0, Array.getLength(a)).mapToObj(idx -> Array.get(a, idx)))
            .iterator()
        )
        .filter(Iterator::hasNext) // filter out empty ValueSource
        .toArray(Iterator[]::new);

    return Arrays
        .stream(testParts)
        .map(t -> t.cases)
        .map(s -> s.apply(instance))
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .map(entry -> Stream
            .concat(
                Stream.of(
                    entry.getKey(),
                    entry.getValue()
                ),
                nextMulti(extraArgIts)
            )
            .toArray()
        )
        .map(Arguments::of);
  }

  private static Stream<Object> nextMulti(Iterator<Object>[] iterators) {
    return Arrays
        .stream(iterators)
        .map(Iterator::next);
  }
}
