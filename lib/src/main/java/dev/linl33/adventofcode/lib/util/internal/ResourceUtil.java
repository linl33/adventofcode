package dev.linl33.adventofcode.lib.util.internal;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ResourceUtil {
  @NotNull
  public static BufferedReader readResource(@NotNull Class<?> clazz, @NotNull String resource) {
    Objects.requireNonNull(clazz);
    Objects.requireNonNull(resource);

    var resourceStream = clazz.getResourceAsStream(resource);

    if (resourceStream == null) {
      handleResourceNotFound(resource);
    }

    return new BufferedReader(new InputStreamReader(
        resourceStream,
        StandardCharsets.UTF_8
    ));
  }

  @NotNull
  public static Path getResourcePath(@NotNull Class<?> clazz, @NotNull String resource) {
    Objects.requireNonNull(clazz);
    Objects.requireNonNull(resource);

    var resourceUrl = clazz.getResource(resource);

    if (resourceUrl == null) {
      handleResourceNotFound(resource);
    }

    try {
      return Paths.get(resourceUrl.toURI());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static void handleResourceNotFound(@NotNull String resource) {
    throw new UncheckedIOException(new FileNotFoundException(resource));
  }
}
