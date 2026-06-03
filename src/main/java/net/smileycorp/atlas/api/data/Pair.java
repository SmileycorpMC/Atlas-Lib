package net.smileycorp.atlas.api.data;

import net.minecraft.util.Tuple;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Pair<T, U> {

    private final T first;
    private final U second;

    private Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public <V, W> Pair<V, W> map(Function<T, V> func1, Function<U, W> func2) {
        return new Pair<>(func1.apply(first), func2.apply(second));
    }

    public <V> Pair<V, U> mapFirst(Function<T, V> func) {
        return new Pair<>(func.apply(first), second);
    }

    public <V> Pair<T, V> mapSecond(Function<U, V> func) {
        return new Pair<>(first, func.apply(second));
    }

    public <V> V apply(BiFunction<T, U, V> func) {
        return func.apply(first, second);
    }

    public void accept(BiConsumer<T, U> consumer) {
        consumer.accept(first, second);
    }

    public Pair<T, U> copy() {
        return new Pair<>(first, second);
    }

    public Tuple<T, U> toTuple() {
        return new Tuple<>(first, second);
    }

    public Map.Entry<T, U> toEntry() {
        return new AbstractMap.SimpleEntry<>(first, second);
    }

    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair<>(first, second);
    }

    public static <T, U> Pair<T, U> of(Pair<T, U> pair) {
        return pair.copy();
    }

    public static <T, U> Pair<T, U> of(Tuple<T, U> tuple) {
        return of(tuple.getFirst(), tuple.getSecond());
    }

    public static <T, U> Pair<T, U> of (Map.Entry<T, U> entry) {
        return of(entry.getKey(), entry.getValue());
    }

}
