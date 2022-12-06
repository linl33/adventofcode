package dev.linl33.adventofcode.testlib;

import dev.linl33.adventofcode.lib.solution.ResourceIdentifier;
import dev.linl33.adventofcode.lib.solution.ResourceService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class StringResourceService implements ResourceService {
  public static final StringResourceService INSTANCE = new StringResourceService();

  private StringResourceService() {
  }

  @Override
  public BufferedReader asBufferedReader(ResourceIdentifier identifier) {
    if (identifier instanceof StringResourceIdentifier sRes) {
      return new BufferedReader(new StringReader(sRes.content()));
    }

    throw new UnsupportedOperationException();
  }

  @Override
  public FileChannel asFileChannel(ResourceIdentifier identifier) {
    if (identifier instanceof StringResourceIdentifier sRes) {
      try {
        var path = Files.createTempFile("", "");
        Files.writeString(path, sRes.content(), StandardCharsets.UTF_8);

        return FileChannel.open(path);
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    throw new UnsupportedOperationException();
  }
}
