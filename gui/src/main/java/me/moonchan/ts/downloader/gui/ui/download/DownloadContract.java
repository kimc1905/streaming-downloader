package me.moonchan.ts.downloader.gui.ui.download;

import javafx.scene.control.TableView;
import me.moonchan.ts.downloader.gui.ui.BaseContract;

public class DownloadContract {
    interface View extends BaseContract.View {

    }
    interface Presenter extends BaseContract.Presenter<View> {
        void bindDownloadTaskTable(TableView<DownloadTaskViewModel> tableDownloadTask);
    }
}