package io;

@FunctionalInterface
public interface UseReader<T, R, X extends Throwable> {
    R apply(T instance) throws X;
}
