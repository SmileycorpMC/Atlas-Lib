package net.smileycorp.atlas.api.data;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<L, R> {
    
    public static <L, R>  Either<L, R> left(L value) {
        return new Left(value);
    }
    
    public static <L, R>  Either<L, R> right(R value) {
        return new Right(value);
    }
    
    public abstract <T> T map(Function<L, T> l, Function<R, T> r);

    public abstract <T> Either<T, R> mapLeft(Function <L, T> func);

    public abstract <T> Either<L, T> mapRight(Function <R, T> func);

    public abstract void accept(Consumer<L> l, Consumer<R> r);

    public abstract boolean isLeft();
    
    private static class Left<L, R> extends Either<L, R> {
    
        private final L value;
    
        private Left(L value) {
            this.value = value;
        }
    
        @Override
        public <T> T map(Function<L, T> l, Function<R, T> r) {
            return l.apply(value);
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<L, T> func) {
            return Either.left(func.apply(value));
        }

        @Override
        public <T> Either<L, T> mapRight(Function<R, T> func) {
            return Either.left(value);
        }

        @Override
        public void accept(Consumer<L> l, Consumer<R> r) {
            l.accept(value);
        }

        @Override
        public boolean isLeft() {
            return true;
        }

    }
    
    private static class Right<L, R> extends Either<L, R> {
        
        private final R value;
        
        private Right(R value) {
            this.value = value;
        }
        
        @Override
        public <T> T map(Function<L, T> l, Function<R, T> r) {
            return r.apply(value);
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<L, T> func) {
            return Either.right(value);
        }

        @Override
        public <T> Either<L, T> mapRight(Function<R, T> func) {
            return Either.right(func.apply(value));
        }

        @Override
        public void accept(Consumer<L> l, Consumer<R> r) {
            r.accept(value);
        }

        @Override
        public boolean isLeft() {
            return true;
        }
        
    }
    
}
