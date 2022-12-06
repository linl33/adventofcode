package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.ClasspathResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.ClasspathResourceService;
import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.ResourceService;

import java.io.BufferedReader;
import java.nio.channels.FileChannel;

public record CompositeResourceService(ClasspathResourceService classpathResourceService,
                                       StringResourceService stringResourceService) implements ResourceService {
  @Override
  public BufferedReader asBufferedReader(ResourceIdentifier identifier) {
    return switch (identifier) {
      case ClasspathResourceIdentifier classpath -> asBufferedReader(classpath);
      case StringResourceIdentifier string -> asBufferedReader(string);
      default -> throw new UnsupportedOperationException();
    };
  }

  @Override
  public FileChannel asFileChannel(ResourceIdentifier identifier) {
    return switch (identifier) {
      case ClasspathResourceIdentifier classpath -> asFileChannel(classpath);
      case StringResourceIdentifier string -> asFileChannel(string);
      default -> throw new UnsupportedOperationException();
    };
  }

  private BufferedReader asBufferedReader(ClasspathResourceIdentifier identifier) {
    return classpathResourceService.asBufferedReader(identifier);
  }

  private BufferedReader asBufferedReader(StringResourceIdentifier identifier) {
    return stringResourceService.asBufferedReader(identifier);
  }

  private FileChannel asFileChannel(ClasspathResourceIdentifier identifier) {
    return classpathResourceService.asFileChannel(identifier);
  }

  private FileChannel asFileChannel(StringResourceIdentifier identifier) {
    return stringResourceService.asFileChannel(identifier);
  }
}
