package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(AdventSolutionExtension.class)
public interface AdventSolutionTest<T1, T2> {
  AdventSolution<T1, T2> newSolutionInstance();

  Map<String, T1> getPart1Cases();

  Map<String, T2> getPart2Cases();

  default Map<TestPart, Map<String, String>> getDisabledTests() {
    return Collections.emptyMap();
  }

  default TestPart[] getTestParts(AdventSolution<T1, T2> instance) {
    return instance.getDay() != 25 ? TestPart.values() : new TestPart[] {TestPart.PART_1};
  }

  default AdventSolution<T1, T2> getSolutionInstanceProxy() {
    return buildSolutionProxy(newSolutionInstance());
  }

  @TestFactory
  @DisplayName("allParts")
  default Stream<DynamicContainer> allParts(AdventSolution<T1, T2> instance,
                                            EnumMap<TestPart, String> defaultResources) {
    return Arrays
        .stream(getTestParts(instance))
        .map(testPart -> dynamicContainer(
            testPart.displayName,
            testPart.testSourceUri.apply(this),
            buildDynamicTestsForPart(testPart, defaultResources)
        ));
  }

  default Stream<DynamicTest> buildDynamicTestsForPart(TestPart testPart,
                                                       EnumMap<TestPart, String> defaultResources) {
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
            kv.getKey(),
            testUri,
            () -> {
              Assumptions.assumeFalse(
                  disabledTests.containsKey(kv.getKey()),
                  () -> "Test Disabled: " + disabledTests.get(kv.getKey())
              );

              buildAssertion(
                  kv.getValue(),
                  testPart.part.apply(getSolutionInstanceProxy(), kv.getKey())
              );
            }
        ));
  }

  @SuppressWarnings("unchecked")
  private AdventSolution<T1, T2> buildSolutionProxy(AdventSolution<T1, T2> delegateTo) {
    // TODO: avoid this proxy

    return (AdventSolution<T1, T2>) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class[] {AdventSolution.class},
        (proxy, method, args) -> {
          if (method.getName().equals("resourceSupplier") && args[0] instanceof String resource && resource.startsWith("string:")) {
            return new BufferedReader(new StringReader(resource.replaceFirst("string:", "")));
          } else {
            try {
              return method.invoke(delegateTo, args);
            } catch (InvocationTargetException e) {
              throw e.getTargetException();
            }
          }
        }
    );
  }

  private static void buildAssertion(Object expected, Object actual) {
    if (expected instanceof Object[] expectedArr && actual instanceof Object[] actualArr) {
      assertArrayEquals(expectedArr, actualArr);
    } else {
      assertEquals(expected, actual);
    }
  }
}
