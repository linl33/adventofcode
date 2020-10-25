package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.AdventSolution;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class AdventSolutionExtension implements ParameterResolver {
  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getTarget().map(AdventSolutionTest.class::isInstance).orElse(false) &&
        AdventSolution.class.isAssignableFrom(parameterContext.getParameter().getType());
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext
        .getTarget()
        .map(o -> o instanceof AdventSolutionTest<?, ?> t ? t.newSolutionInstance() : null)
        .orElseThrow(() -> new ParameterResolutionException("Cannot cast to AdventSolutionTest<?, ?>"));
  }
}
