package dev.linl33.adventofcode.lib.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
}
