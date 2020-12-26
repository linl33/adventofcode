package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(AdventSolutionExtension.class)
public interface AdventSolutionTest<T1, T2> {
  AdventSolution<T1, T2> newSolutionInstance();

  Map<Object, T1> getPart1Cases();

  Map<Object, T2> getPart2Cases();

  default Map<ResourceIdentifier, T1> getPart1Resources() {
    return adaptCaseMap(getPart1Cases());
  }

  default Map<ResourceIdentifier, T2> getPart2Resources() {
    return adaptCaseMap(getPart2Cases());
  }

  default AdventSolution<T1, T2> getSolutionInstance() {
    var instance = (AdventSolution<T1, T2> & ClasspathResourceService & ResourceServiceHolder) newSolutionInstance();
    instance.setResourceService(new CompositeResourceService(instance, new StringResourceService()));

    return instance;
  }

  default Map<TestPart, Map<ResourceIdentifier, String>> getDisabledTests() {
    return Collections.emptyMap();
  }

  default TestPart[] getTestParts(AdventSolution<T1, T2> instance) {
    return instance.getDay() != 25 ? TestPart.values() : new TestPart[] {TestPart.PART_1};
  }

  @TestFactory
  @DisplayName("allParts")
  default Stream<DynamicContainer> allParts(AdventSolution<T1, T2> instance,
                                            EnumMap<TestPart, ResourceIdentifier> defaultResources) {
    return Arrays
        .stream(getTestParts(instance))
        .map(testPart -> dynamicContainer(
            testPart.displayName,
            testPart.testSourceUri.apply(this),
            buildDynamicTestsForPart(testPart, defaultResources)
        ));
  }

  default Stream<DynamicTest> buildDynamicTestsForPart(TestPart testPart,
                                                       EnumMap<TestPart, ResourceIdentifier> defaultResources) {
    var cases = testPart.cases.apply(this);
    var defaultResource = defaultResources.get(testPart);
    assertTrue(
        cases.containsKey(defaultResource),
        "Default resource (" + defaultResource + ") not covered by " + testPart.displayName
    );

    var testUri = testPart.testSourceUri.apply(this);
    var disabledTests = getDisabledTests().getOrDefault(testPart, Collections.emptyMap());

    return cases
        .entrySet()
        .stream()
        .map(kv -> dynamicTest(
            kv.getKey().toString(),
            testUri,
            () -> {
              Assumptions.assumeFalse(
                  disabledTests.containsKey(kv.getKey()),
                  () -> "Test Disabled: " + disabledTests.get(kv.getKey())
              );

              buildAssertion(
                  kv.getValue(),
                  testPart.part.apply(getSolutionInstance(), kv.getKey())
              );
            }
        ));
  }

  private static void buildAssertion(Object expected, Object actual) {
    if (expected instanceof Object[] expectedArr && actual instanceof Object[] actualArr) {
      assertArrayEquals(expectedArr, actualArr);
    } else {
      assertEquals(expected, actual);
    }
  }

  private static <T> Map<ResourceIdentifier, T> adaptCaseMap(Map<Object, T> caseMap) {
    return caseMap
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            kv -> resolveIdentifier(kv.getKey()),
            Map.Entry::getValue
        ));
  }

  private static ResourceIdentifier resolveIdentifier(Object obj) {
    if (obj instanceof String strKey) {
      return strKey.startsWith("string:") ?
          new StringResourceIdentifier(strKey) :
          new ClasspathResourceIdentifier(strKey);
    } else if (obj instanceof ResourceIdentifier resId) {
      return resId;
    } else {
      throw new IllegalArgumentException();
    }
  }
}
