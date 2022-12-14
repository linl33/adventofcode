package dev.linl33.adventofcode.lib;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public record ByteBufferAsCharSequence(ByteBuffer buffer) implements CharSequence {
  @Override
  public int length() {
    return buffer.remaining();
  }

  @Override
  public char charAt(int index) {
    return (char) buffer.get(buffer.position() + index);
  }

  @Override
  public boolean isEmpty() {
    return !buffer.hasRemaining();
  }

  @NotNull
  @Override
  public CharSequence subSequence(int start, int end) {
    var subSequenceStart = buffer.position() + start;
    return new ByteBufferAsCharSequence(buffer.slice(subSequenceStart, end - subSequenceStart));
  }

  @Override
  public String toString() {
    var arr = new byte[length()];
    buffer.duplicate().get(buffer.position(), arr);
    return new String(arr, 0, arr.length, StandardCharsets.UTF_8);
  }
}
