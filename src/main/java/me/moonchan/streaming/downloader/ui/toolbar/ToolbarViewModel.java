package me.moonchan.streaming.downloader.ui.toolbar;

import javafx.event.ActionEvent;
import me.moonchan.streaming.downloader.util.EventBus;

public class ToolbarViewModel {
    public void publishClickEvent(ActionEvent e) {
        EventBus.get().post(e);
    }
}
