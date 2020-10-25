package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.SolutionFunction;
import dev.linl33.adventofcode.lib.solution.SolutionPart;
import dev.linl33.adventofcode.lib.solution.SupplierWithCtx;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public enum TestPart {
  PART_1(
      SolutionPart.PART_1,
      AdventSolutionTest::getPart1Cases,
      testSourceUriFactory("getPart1Cases"),
      "Part 1"
  ),
  PART_2(
      SolutionPart.PART_2,
      AdventSolutionTest::getPart2Cases,
      testSourceUriFactory("getPart2Cases"),
      "Part 2"
  );

  public final SolutionFunction<?> part;
  public final SupplierWithCtx<String> defaultResource;
  public final SupplierWithTestCtx<Map<String, ?>> cases;
  public final SupplierWithTestCtx<URI> testSourceUri;
  public final String displayName;

  TestPart(SolutionPart parent,
           SupplierWithTestCtx<Map<String, ?>> cases,
           SupplierWithTestCtx<URI> testSourceUri,
           String displayName) {
    this.part = parent.part;
    this.defaultResource = parent.defaultResource;
    this.cases = cases;
    this.testSourceUri = testSourceUri;
    this.displayName = displayName;
  }

  private static SupplierWithTestCtx<URI> testSourceUriFactory(String methodName) {
    return solutionTest -> {
      try {
        return new URI("method", solutionTest.getClass().getCanonicalName(), methodName);
      } catch (URISyntaxException e) {
        throw new IllegalArgumentException(e);
      }
    };
  }
}
