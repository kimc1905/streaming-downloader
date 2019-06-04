package me.moonchan.streaming.downloader.ui.toolbar;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import me.moonchan.streaming.downloader.util.Constants;

public class ToolbarViewModel {

    private final Relay<String> observableToolbarEvent;

    public ToolbarViewModel() {
        observableToolbarEvent = PublishRelay.create();
    }

    public Relay<String> getRelayToolbarEvent() {
        return observableToolbarEvent;
    }

    public void publishAddEvent() {
        observableToolbarEvent.accept(Constants.Event.ADD_DOWNLOAD_TASK);
    }

    public void publishClearEvent() {
        observableToolbarEvent.accept(Constants.Event.CLEAR_FINISHED_TASK);
    }
}
