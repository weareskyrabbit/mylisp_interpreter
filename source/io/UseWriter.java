package io;

@FunctionalInterface
public interface UseWriter<T, X extends Throwable> {
    void accept(T instance) throws X;
}
