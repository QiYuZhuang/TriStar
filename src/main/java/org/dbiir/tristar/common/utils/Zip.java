package org.dbiir.tristar.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Zip {
    public record Triple<T, U, V>(T first, U second, V third) {}
    public record Pair<T, U>(T first, U second) {}

    public static <T, U> Iterable<Pair<T, U>> zip(Iterable<T> first, Iterable<U> second) {
        return pairInternal(first, second);
    }

    public static <T, U, V> Iterable<Triple<T, U, V>> zip(Iterable<T> first, Iterable<U> second, Iterable<V> third) {
        return tripleInternal(first, second, third);
    }

    private static <T, U> Iterable<Pair<T, U>> pairInternal(Iterable<T> first, Iterable<U> second) {
        List<Iterator<?>> iterators = new ArrayList<>();
        iterators.add(first.iterator());
        iterators.add(second.iterator());

        List<Pair<T, U>> pairs = new ArrayList<>();
        while (true) {
            List<Object> values = new ArrayList<>();
            for (Iterator<?> iterator : iterators) {
                if (!iterator.hasNext()) {
                    return pairs;
                }
                values.add(iterator.next());
            }

            pairs.add(new Pair<>((T) values.get(0), (U) values.get(1)));
        }
    }

    public static <T, U, V> Iterable<Triple<T, U, V>> tripleInternal(Iterable<T> first, Iterable<U> second, Iterable<V> third) {
        List<Iterator<?>> iterators = new ArrayList<>();
        iterators.add(first.iterator());
        iterators.add(second.iterator());
        iterators.add(third.iterator());

        List<Triple<T, U, V>> triples = new ArrayList<>();
        while (true) {
            List<Object> values = new ArrayList<>();
            for (Iterator<?> iterator : iterators) {
                if (!iterator.hasNext()) {
                    return triples;
                }
                values.add(iterator.next());
            }
            if (values.get(0) != null && values.get(1) != null)
                triples.add(new Triple<>((T) values.get(0), (U) values.get(1), (V) values.get(2)));
        }
    }
}
