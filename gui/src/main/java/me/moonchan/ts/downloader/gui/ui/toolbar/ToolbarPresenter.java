package me.moonchan.ts.downloader.gui.ui.toolbar;

import javafx.event.ActionEvent;
import me.moonchan.ts.downloader.gui.event.ToolbarEvent;
import me.moonchan.ts.downloader.gui.util.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ToolbarPresenter implements ToolbarContract.Presenter {
    private EventBus eventBus;

    @Autowired
    public ToolbarPresenter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void sendEventMessage(ActionEvent e) {
        eventBus.send(ToolbarEvent.of(e));
    }

    @Override
    public void setView(ToolbarContract.View view) {

    }
}