package me.moonchan.ts.downloader.gui.ui.toolbar;

import javafx.event.ActionEvent;
import me.moonchan.ts.downloader.gui.ui.BaseContract;

class ToolbarContract {
    interface View extends BaseContract.View {
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void sendEventMessage(ActionEvent e);
    }
}