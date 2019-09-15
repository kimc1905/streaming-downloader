package me.moonchan.streaming.downloader.ui.download;

import javafx.scene.control.TableView;
import me.moonchan.streaming.downloader.ui.BaseContract;

public class DownloadContract {
    interface View extends BaseContract.View {

    }
    interface Presenter extends BaseContract.Presenter<View> {
        void bindDownloadTaskTable(TableView<DownloadTaskViewModel> tableDownloadTask);
    }
}