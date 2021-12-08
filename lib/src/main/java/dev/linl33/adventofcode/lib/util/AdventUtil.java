package dev.linl33.adventofcode.lib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.BufferedReader;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class AdventUtil {
  public static <K, V extends Comparable<V>> K argMax(Map<K, V> map) {
    return argMax(map, Comparator.naturalOrder());
  }

  public static <K, V> K argMax(Map<K, V> map, Comparator<? super V> comparator) {
    Iterator<Map.Entry<K, V>> mapIt = map.entrySet().iterator();
    Map.Entry<K, V> first = mapIt.next();

    V maxVal = first.getValue();
    K maxKey = first.getKey();

    while (mapIt.hasNext()) {
      Map.Entry<K, V> next = mapIt.next();

      if (comparator.compare(next.getValue(), maxVal) > 0) {
        maxVal = next.getValue();
        maxKey = next.getKey();
      }
    }

    return maxKey;
  }

  public static <K, V extends Comparable<V>> K argMax(Collection<K> collection, Function<K, V> mapping) {
    return argMax(collection, mapping, Comparator.naturalOrder());
  }

  public static <K, V> K argMax(Collection<K> collection, Function<K, V> mapping, Comparator<? super V> comparator) {
    var collectionIt = collection.iterator();

    var maxKey = collectionIt.next();
    var maxVal = mapping.apply(maxKey);

    var size = collection.size() - 1;
    for (int i = 0; i < size; i++) {
      var nextKey = collectionIt.next();
      var nextVal = mapping.apply(nextKey);

      if (comparator.compare(nextVal, maxVal) > 0) {
        maxVal = nextVal;
        maxKey = nextKey;
      }
    }

    return maxKey;
  }

  public static <K, V extends Comparable<V>> K argMin(Map<K, V> map) {
    return argMax(map, Comparator.reverseOrder());
  }

  public static <K, V> K argMin(Map<K, V> map, Comparator<V> comparator) {
    return argMax(map, comparator.reversed());
  }

  public static <K, V extends Comparable<V>> K argMin(Collection<K> collection, Function<K, V> mapping) {
    return argMax(collection, mapping, Comparator.reverseOrder());
  }

  public static <K, V> K argMin(Collection<K> collection, Function<K, V> mapping, Comparator<V> comparator) {
    return argMax(collection, mapping, comparator.reversed());
  }

  public static <K> Integer incrementMap(Map<K, Integer> counters, K key) {
    return counters.compute(key, AdventUtil::increment);
  }

  public static <K> Integer increment(K key, Integer value) {
    return value == null ? 1 : value + 1;
  }

  public static <K> Map<K, Long> buildFreqMap(Collection<K> iterable) {
    return iterable
        .stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }

  public static int alphabetToInt(char c) {
    return c - (c < 'a' ? ('A' - 26) : 'a');
  }

  public static char intToAlphabet(int i) {
    return (char) (i + (i > 25 ? ('A' - 26) : 'a'));
  }

  public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
    return map
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getValue,
            Map.Entry::getKey
        ));
  }

  @NotNull
  public static <T> List<? extends Collection<T>> generateCombinations(@NotNull List<T> choices) {
    if (choices.isEmpty()) {
      return new ArrayList<>(new ArrayList<>());
    }

    var choiceCount = choices.size();
    if (choiceCount < Integer.SIZE - 2) {
      return generateCombinationsSmall(choices, choiceCount);
    }

    var prevList = choices.stream().map(Set::of).collect(Collectors.toUnmodifiableSet());
    var result = new ArrayList<>(prevList);

    for (int i = 1; i < choiceCount; i++) {
      var newList = new HashSet<Set<T>>();

      for (T choice : choices) {
        for (Set<T> ts : prevList) {
          if (!ts.contains(choice)) {
            var newSet = new HashSet<>(ts);
            newSet.add(choice);

            newList.add(newSet);
          }
        }
      }

      result.addAll(newList);
      prevList = newList;
    }

    return result;
  }

  @NotNull
  private static <T> List<List<T>> generateCombinationsSmall(@NotNull List<T> choices,
                                                             @Range(from = 1, to = Integer.SIZE - 2) int choiceCount) {
    var result = new ArrayList<List<T>>((1 << choiceCount) - 1);

    @SuppressWarnings("unchecked")
    var allChoices = (T[][]) new Object[1 << choiceCount][];
    var prevIdx = new int[choiceCount];

    for (int i = 0; i < choiceCount; i++) {
      @SuppressWarnings("unchecked")
      var choiceArr = (T[]) new Object[] {choices.get(i)};

      allChoices[1 << i] = choiceArr;
      prevIdx[i] = 1 << i;
      result.add(Arrays.asList(choiceArr));
    }

    for (int i = 2; i <= choiceCount; i++) {
      var newIdx = new int[MathUtil.choose(choiceCount, i)];
      var counter = 0;

      for (int j = 0; j < choiceCount; j++) {
        var currChoice = choices.get(j);

        for (int idx : prevIdx) {
          int nextIdx;
          if ((nextIdx = (idx | (1 << j))) == idx || allChoices[nextIdx] != null) {
            continue;
          }

          @SuppressWarnings("unchecked")
          var choiceArr = (T[]) new Object[i];
          choiceArr[i - 1] = currChoice;
          System.arraycopy(allChoices[idx], 0, choiceArr, 0, i - 1);

          allChoices[nextIdx] = choiceArr;
          newIdx[counter++] = nextIdx;
          result.add(Arrays.asList(choiceArr));

          if (counter == newIdx.length) {
            break;
          }
        }

        if (counter == newIdx.length) {
          break;
        }
      }

      prevIdx = newIdx;
    }

    return result;
  }

  public static Stream<Stream<String>> readInputGrouped(@NotNull BufferedReader reader) {
    return readInputGrouped(reader, String::isEmpty);
  }

  public static Stream<Stream<String>> readInputGrouped(@NotNull BufferedReader reader,
                                                        @NotNull Predicate<String> endOfGroup) {
    return AdventUtil.<Stream.Builder<String>, Stream<String>>readInputGrouped(
        reader,
        endOfGroup,
        Stream::builder,
        Stream.Builder::accept,
        Stream.Builder::build
    );
  }

  public static <A, T> Stream<T> readInputGrouped(@NotNull BufferedReader reader,
                                                  @NotNull Predicate<String> endOfGroup,
                                                  @NotNull Supplier<A> supplier,
                                                  @NotNull BiConsumer<A, String> accumulator,
                                                  @NotNull Function<A, T> finisher) {
    var inputIt = reader
        .lines()
        .iterator();

    return Stream
        .generate(() -> {
          if (!inputIt.hasNext()) {
            return null;
          }

          var accu = supplier.get();

          String token;
          while (inputIt.hasNext() && !endOfGroup.test(token = inputIt.next())) {
            accumulator.accept(accu, token);
          }

          return finisher.apply(accu);
        })
        .takeWhile(Objects::nonNull);
  }

  public static int[] readDelimiterSeperatedInts(@NotNull String input, char delimiter, int maxSize) {
    var ints = new int[maxSize];
    var start = 0;
    var length = input.length();
    var outputCounter = 0;
    for (int i = 0; i < length; i++) {
      if (input.charAt(i) == delimiter) {
        ints[outputCounter++] = Integer.parseInt(input, start, i, 10);
        start = i + 1;
      }
    }

    ints[outputCounter++] = Integer.parseInt(input, start, length, 10);
    return maxSize == outputCounter ? ints : Arrays.copyOf(ints, outputCounter);
  }
}
