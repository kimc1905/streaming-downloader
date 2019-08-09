package me.moonchan.streaming.downloader.ui.main;

import me.moonchan.streaming.downloader.ui.BaseContract;
import me.moonchan.streaming.downloader.ui.addtask.AddDownloadTaskContract;

public class MainContract {
    interface View extends BaseContract.View {
        void showAddDownloadTaskDialog();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void initAddDownloadTaskView(AddDownloadTaskContract.View addDownloadTaskView);
        void shutdownDownloader();
    }
}
