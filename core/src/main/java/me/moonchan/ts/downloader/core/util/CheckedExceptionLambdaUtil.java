package me.moonchan.ts.downloader.core.util;

import java.util.function.Consumer;

public class CheckedExceptionLambdaUtil {
    public static <T> Consumer<T> throwingConsumerWrapper(
            ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
