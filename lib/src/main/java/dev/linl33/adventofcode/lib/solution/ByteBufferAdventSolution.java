package dev.linl33.adventofcode.lib.solution;

import dev.linl33.adventofcode.lib.function.ThrowingBiFunction;
import dev.linl33.adventofcode.lib.function.ThrowingFunction;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public interface ByteBufferAdventSolution<T1, T2> extends AdventSolution<T1, T2>, ResourceServiceHolder {
  @Override
  default T1 part1(ResourceIdentifier identifier) throws Exception {
    return SolutionUtil.runWithResource(identifier, getResourceService()::asFileChannel, adaptFileChannel(this::part1));
  }

  @Override
  default T2 part2(ResourceIdentifier identifier) throws Exception {
    return SolutionUtil.runWithResource(identifier, getResourceService()::asFileChannel, adaptFileChannel(this::part2));
  }

  @Override
  default <U, A extends AdventSolution<T1, T2>> U run(ThrowingBiFunction<A, ?, U> customPart, ResourceIdentifier resource) {
    return (U) SolutionUtil.adaptPartToRun(
        this,
        convert((ThrowingBiFunction<ByteBufferAdventSolution<T1, T2>, ByteBuffer, ?>) customPart),
        resource
    );
  }

  @Override
  default <U, A extends AdventSolution<T1, T2>> void print(ThrowingBiFunction<A, ?, U> customPart,
                                                           ThrowingBiFunction<A, U, ?> printMapping,
                                                           ResourceIdentifier resource) {
    SolutionUtil.adaptPartToPrint(
        this,
        convert((ThrowingBiFunction<ByteBufferAdventSolution<T1, T2>, ByteBuffer, ?>) customPart),
        (ThrowingBiFunction<ByteBufferAdventSolution<T1, T2>, Object, ?>) printMapping,
        resource
    );
  }

  default T1 part1(ByteBuffer byteBuffer) throws Exception {
    return null;
  }

  default T2 part2(ByteBuffer byteBuffer) throws Exception {
    return null;
  }

  private static <T> ThrowingFunction<FileChannel, T> adaptFileChannel(ThrowingFunction<ByteBuffer, T> byteBufferFunc) {
    return byteBufferFunc.compose(ByteBufferAdventSolution::mapWholeFileRO);
  }

  private static MappedByteBuffer mapWholeFileRO(FileChannel channel) throws IOException {
    return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
  }

  private <A extends ByteBufferAdventSolution<T1, T2>, T> ThrowingBiFunction<A, ResourceIdentifier, T> convert(ThrowingBiFunction<A, ByteBuffer, T> orig) {
    return (instance, identifier) -> SolutionUtil.runWithResource(
        instance,
        identifier,
        (a, resId) -> a.getResourceService().asFileChannel(resId),
        (a, channel) -> orig.apply(a, mapWholeFileRO(channel))
    );
  }
}
