package me.moonchan.streaming.downloader.ui.toolbar;

import javafx.event.ActionEvent;
import me.moonchan.streaming.downloader.ui.BaseContract;

class ToolbarContract {

    interface View extends BaseContract.View {
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void sendEventMessage(ActionEvent e);
    }
}
