package net.smileycorp.atlas.api.util;

public interface TriFunction<T, U, V, R> {

	public R apply(T t, U u, V v);

}
