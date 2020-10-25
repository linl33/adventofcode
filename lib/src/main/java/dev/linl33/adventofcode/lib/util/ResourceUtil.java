package dev.linl33.adventofcode.lib.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceUtil {
  public static BufferedReader readResource(Class<?> clazz, String resource) {
    Objects.requireNonNull(clazz);
    Objects.requireNonNull(resource);

    return new BufferedReader(new InputStreamReader(
        clazz.getResourceAsStream(resource),
        StandardCharsets.UTF_8
    ));
  }
}
