package dev.linl33.adventofcode.testlib;

import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@ArgumentsSource(TestCaseArgumentsProvider.class)
public @interface TestCaseSource {
  TestPart[] value();

  ValueSource[] extraArgs() default @ValueSource;
}
