package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.Function;
import java.util.stream.Collectors;

class AdventSolutionExtension implements ParameterResolver {
  public static final Namespace NAMESPACE = Namespace.create(AdventSolutionExtension.class);

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    if (!parameterContext.getTarget().map(AdventSolutionTest.class::isInstance).orElse(false)) {
      return false;
    }

    if (AdventSolution.class.isAssignableFrom(parameterContext.getParameter().getType())) {
      return true;
    }

    if (!EnumMap.class.isAssignableFrom(parameterContext.getParameter().getType())) {
      return false;
    }

    var actualTypeArguments =
        ((ParameterizedType) parameterContext.getParameter().getParameterizedType()).getActualTypeArguments();

    return actualTypeArguments.length == 2 &&
        actualTypeArguments[0].equals(TestPart.class) &&
        actualTypeArguments[1].equals(ResourceIdentifier.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
                                 ExtensionContext extensionContext) throws ParameterResolutionException {
    if (AdventSolution.class.isAssignableFrom(parameterContext.getParameter().getType())) {
      return resolveAdventSolutionInstance(parameterContext);
    }

    if (EnumMap.class.isAssignableFrom(parameterContext.getParameter().getType())) {
      return resolveDefaultResourceMap(extensionContext);
    }

    throw new ParameterResolutionException("Cannot resolve parameter");
  }

  private static AdventSolution<?, ?> resolveAdventSolutionInstance(ParameterContext parameterContext) {
    return parameterContext
        .getTarget()
        .map(o -> o instanceof AdventSolutionTest<?, ?> t ? t.getSolutionInstance() : null)
        .orElseThrow(() -> new ParameterResolutionException("Cannot cast to AdventSolutionTest<?, ?>"));
  }

  private static EnumMap<TestPart, ResourceIdentifier> resolveDefaultResourceMap(ExtensionContext extensionContext) {
    var resourceRetrievalInstance = (AdventSolutionTest<?, ?>) extensionContext
        .getStore(NAMESPACE)
        .getOrComputeIfAbsent(extensionContext.getRequiredTestClass());

    return getDefaultTestResources(resourceRetrievalInstance);
  }

  private static <T1, T2> EnumMap<TestPart, ResourceIdentifier> getDefaultTestResources(AdventSolutionTest<T1, T2> testInstance) {
    var instance = testInstance.newSolutionInstance();

    return Arrays
        .stream(testInstance.getTestParts(instance))
        .collect(Collectors.toMap(
            Function.identity(),
            testPart -> testPart.defaultResource.apply(instance),
            (r1, r2) -> r1,
            () -> new EnumMap<>(TestPart.class)
        ));
  }
}
