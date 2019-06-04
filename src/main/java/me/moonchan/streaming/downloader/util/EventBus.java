package me.moonchan.streaming.downloader.util;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import io.reactivex.Observable;

public class EventBus {

    private static EventBus ourInstance = new EventBus();
    private final Relay<Object> relay;

    private EventBus() {
        relay = PublishRelay.create();
    }

    public static EventBus get() {
        return ourInstance;
    }

    public void post(Object value) {
        relay.accept(value);
    }

    public Observable<Object> getObservable() {
        return relay;
    }

    public <T> Observable<T> getObservable(Class<T> tClass) {
        return relay.filter(value -> tClass.isInstance(value))
                .map(value -> tClass.cast(value));
    }
}
