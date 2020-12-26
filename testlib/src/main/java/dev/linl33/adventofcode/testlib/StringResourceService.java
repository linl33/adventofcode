package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.ResourceService;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.channels.FileChannel;

public class StringResourceService implements ResourceService {
  @Override
  public BufferedReader asBufferedReader(ResourceIdentifier identifier) {
    if (identifier instanceof StringResourceIdentifier sRes) {
      return new BufferedReader(new StringReader(sRes.content()));
    }

    throw new UnsupportedOperationException();
  }

  @Override
  public FileChannel asFileChannel(ResourceIdentifier identifier) {
    throw new UnsupportedOperationException();
  }
}
