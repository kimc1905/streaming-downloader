package me.moonchan.ts.downloader.core.util;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t)throws E;
}
