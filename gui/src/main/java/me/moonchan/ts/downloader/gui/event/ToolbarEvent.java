package me.moonchan.ts.downloader.gui.event;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.stage.Window;

public class ToolbarEvent {
    private ActionEvent event;

    private ToolbarEvent(ActionEvent e) {
        this.event = e;
    }

    public static ToolbarEvent of(ActionEvent e) {
        return new ToolbarEvent(e);
    }

    public boolean isEqualSource(String id) {
        return getSourceId().equals(id);
    }

    private String getSourceId() {
        return ((Control) event.getSource()).getId();
    }

    public Window getSourceWindow() {
        return ((Node) event.getSource()).getScene().getWindow();
    }
}