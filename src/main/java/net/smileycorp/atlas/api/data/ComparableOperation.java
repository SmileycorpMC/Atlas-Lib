package net.smileycorp.atlas.api.data;

import java.util.function.BiFunction;

public enum ComparableOperation {

	EQUALS("==", (a, b) -> a==b || a.equals(b)),
	NOT_EQUALS("!=", (a, b) -> !(a==b || a.equals(b))),
	LESS_THAN("<", (a, b) -> numberWrapped(a, b, (c, d)->c.compareTo(d)<0)),
	GREATER_THAN(">", (a, b) -> numberWrapped(a, b, (c, d)->c.compareTo(d)>0)),
	LESS_OR_EQUAL("<=", (a, b) -> numberWrapped(a, b, (c, d)->c.compareTo(d)<=0)),
	GREATER_OR_EQUAL(">=", (a, b) -> numberWrapped(a, b, (c, d)->c.compareTo(d)>=0));

	private final String symbol;
	private final BiFunction<Comparable<?>, Comparable<?>, Boolean> operation;

	private ComparableOperation(String symbol, BiFunction<Comparable<?>, Comparable<?>, Boolean> operation) {
		this.symbol = symbol;
		this.operation = operation;
	}

	public String getSymbol() {
		return symbol;
	}

	public boolean apply(Comparable<?> a, Comparable<?> b) {
		return operation.apply(a, b);
	}

	public static ComparableOperation of(String symbol) {
		for (ComparableOperation operation : values()) {
			if (operation.getSymbol().equals(symbol)) return operation;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static boolean numberWrapped(Comparable<?> a, Comparable<?> b, BiFunction<Comparable<Number>, Number, Boolean> wrappedFunction) {
		if (a instanceof Number && b instanceof Number) {
			return wrappedFunction.apply((Comparable<Number>)a, (Number)b);
		}
		return false;
	}

}
