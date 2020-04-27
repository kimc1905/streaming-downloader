package me.moonchan.ts.downloader.gui.util;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;

public class EventBus {

    private final Relay<Object> relay;

    public EventBus() {
        relay = PublishRelay.create().toSerialized();
    }

    public void send(Object event) {
        relay.accept(event);
    }

    public boolean hasObservers() {
        return relay.hasObservers();
    }

    public <T> Observable<T> ofType(Class<T> eventType) {
        return relay.ofType(eventType);
    }

    public <T> Disposable register(Class<T> eventType, Scheduler scheduler, Consumer<T> onNext) {
        return ofType(eventType).observeOn(scheduler).subscribe(onNext);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext) {
        return ofType(eventType).observeOn(JavaFxScheduler.platform()).subscribe(onNext);
    }

    public <T> Disposable register(Class<T> eventType, Consumer<T> onNext, Consumer<Throwable> onError) {
        return ofType(eventType).observeOn(JavaFxScheduler.platform()).subscribe(onNext, onError);
    }

    public void unregister(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}