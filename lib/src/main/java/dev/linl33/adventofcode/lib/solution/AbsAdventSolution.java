package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.util.internal.ResourceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public abstract class AbsAdventSolution<T1, T2> implements
    BufferedReaderAdventSolution<T1, T2>, ClasspathResourceService, ResourceServiceHolder {
  private final Logger logger;
  private final ClasspathResourceIdentifier defaultResourceIdentifier;
  private final Path defaultResourcePath;
  private ResourceService resourceService;

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public ResourceService getResourceService() {
    return resourceService;
  }

  @Override
  public void setResourceService(ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  protected AbsAdventSolution() {
    logger = LogManager.getLogger(getClass());
    defaultResourceIdentifier = new ClasspathResourceIdentifier(getClass().getSimpleName().toLowerCase(Locale.ROOT));
    defaultResourcePath = ResourceUtil.getResourcePath(getClass(), defaultResourceIdentifier.name());
    resourceService = this;
  }

  @Override
  public T1 part1(@NotNull ResourceIdentifier identifier) throws Exception {
    return BufferedReaderAdventSolution.super.part1(identifier);
  }

  @Override
  public T2 part2(@NotNull ResourceIdentifier identifier) throws Exception {
    return BufferedReaderAdventSolution.super.part2(identifier);
  }

  @Override
  public <U, A extends AdventSolution<T1, T2>> U run(ThrowingBiFunction<A, ?, U> customPart,
                                                     ResourceIdentifier resource) {
    return BufferedReaderAdventSolution.super.run(customPart, resource);
  }

  @Override
  public <U, A extends AdventSolution<T1, T2>> void print(ThrowingBiFunction<A, ?, U> customPart,
                                                          ThrowingBiFunction<A, U, ?> printMapping,
                                                          ResourceIdentifier resource) {
    BufferedReaderAdventSolution.super.print(customPart, printMapping, resource);
  }

  @Override
  public int getDay() {
    return Integer.parseInt(getClass().getSimpleName().substring(3));
  }

  @Override
  @NotNull
  public ResourceIdentifier getDefaultResource() {
    return defaultResourceIdentifier;
  }

  @Override
  public BufferedReader asBufferedReader(ResourceIdentifier identifier) {
    if (identifier == defaultResourceIdentifier) {
      try {
        return Files.newBufferedReader(defaultResourcePath, StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    if (identifier instanceof ClasspathResourceIdentifier classpathRes) {
      return ResourceUtil.readResource(getClass(), classpathRes.name());
    }

    throw new IllegalArgumentException("Unable to resolve identifier " + identifier);
  }

  @Override
  public FileChannel asFileChannel(ResourceIdentifier identifier) {
    Path resPath;

    if (identifier == defaultResourceIdentifier) {
      resPath = defaultResourcePath;
    } else if (identifier instanceof ClasspathResourceIdentifier classpathRes) {
      resPath = ResourceUtil.getResourcePath(getClass(), classpathRes.name());
    } else {
      throw new IllegalArgumentException("Unable to resolve identifier " + identifier);
    }

    return pathToFileChannel(resPath);
  }

  private static FileChannel pathToFileChannel(Path path) {
    try {
      return FileChannel.open(path);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
