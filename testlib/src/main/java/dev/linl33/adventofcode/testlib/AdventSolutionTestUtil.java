package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdventSolutionTestUtil {
  private static final String DISABLE_REASON_SLOW = "Too Slow";

  public static Map<TestPart, Map<ResourceIdentifier, String>> disableSlowTest(Map<TestPart, Map<ResourceIdentifier, String>> currDisabledTests,
                                                                               TestPart toDisable,
                                                                               ResourceIdentifier testCaseResource) {
    if (currDisabledTests.getOrDefault(toDisable, Collections.emptyMap()).containsKey(testCaseResource)) {
      return currDisabledTests;
    }

    if (currDisabledTests.isEmpty()) {
      return Map.of(toDisable, Map.of(testCaseResource, DISABLE_REASON_SLOW));
    }

    var relevantPart = currDisabledTests.getOrDefault(toDisable, Collections.emptyMap());
    @SuppressWarnings("unchecked")
    var newEntries = (Map.Entry<ResourceIdentifier, String>[]) new Map.Entry[relevantPart.size() + 1];
    relevantPart.entrySet().toArray(newEntries);
    newEntries[relevantPart.size()] = Map.entry(testCaseResource, DISABLE_REASON_SLOW);

    return Stream
        .concat(
            currDisabledTests.entrySet().stream(),
            Stream.of(Map.entry(toDisable, Map.ofEntries(newEntries)))
        )
        .collect(Collectors.toUnmodifiableMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (left, right) -> left.containsKey(testCaseResource) ? left : right
        ));
  }

  public static Map<TestPart, Map<ResourceIdentifier, String>> disableSlowDefaultTest(TestPart toDisable,
                                                                                      AdventSolutionTest<?, ?> test) {
    return disableSlowTest(
        Collections.emptyMap(),
        toDisable,
        toDisable.defaultResource.apply(test.newSolutionInstance())
    );
  }

  public static Map<TestPart, Map<ResourceIdentifier, String>> disableSlowPart1(AdventSolutionTest<?, ?> test) {
    return disableSlowDefaultTest(TestPart.PART_1, test);
  }

  public static Map<TestPart, Map<ResourceIdentifier, String>> disableSlowPart2(AdventSolutionTest<?, ?> test) {
    return disableSlowDefaultTest(TestPart.PART_2, test);
  }
}
