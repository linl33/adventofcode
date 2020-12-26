package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.*;

import java.io.BufferedReader;
import java.nio.channels.FileChannel;

public record CompositeResourceService(ClasspathResourceService classpathResourceService,
                                       StringResourceService stringResourceService) implements ResourceService {
  @Override
  public BufferedReader asBufferedReader(ResourceIdentifier identifier) {
    if (identifier instanceof ClasspathResourceIdentifier classpath) {
      return asBufferedReader(classpath);
    } else if (identifier instanceof StringResourceIdentifier string) {
      return asBufferedReader(string);
    }

    throw new UnsupportedOperationException();
  }

  @Override
  public FileChannel asFileChannel(ResourceIdentifier identifier) {
    if (identifier instanceof ClasspathResourceIdentifier classpath) {
      return asFileChannel(classpath);
    }

    throw new UnsupportedOperationException();
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
}
