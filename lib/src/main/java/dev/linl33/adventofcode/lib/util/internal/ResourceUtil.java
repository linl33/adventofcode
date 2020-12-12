package dev.linl33.adventofcode.lib.util.internal;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceUtil {
  @NotNull
  public static BufferedReader readResource(@NotNull Class<?> clazz, @NotNull String resource) {
    Objects.requireNonNull(clazz);
    Objects.requireNonNull(resource);

    var resourceStream = clazz.getResourceAsStream(resource);

    if (resourceStream == null) {
      throw new UncheckedIOException(new FileNotFoundException(resource));
    }

    return new BufferedReader(new InputStreamReader(
        resourceStream,
        StandardCharsets.UTF_8
    ));
  }
}
