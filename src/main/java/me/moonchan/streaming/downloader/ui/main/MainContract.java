package me.moonchan.streaming.downloader.ui.main;

import me.moonchan.streaming.downloader.ui.BaseContract;

public class MainContract {
    interface View extends BaseContract.View {
        void showAddDownloadTaskDialog();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void shutdownDownloader();
    }
}
