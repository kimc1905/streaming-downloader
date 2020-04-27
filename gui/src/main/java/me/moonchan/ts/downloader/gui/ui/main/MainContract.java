package me.moonchan.ts.downloader.gui.ui.main;

import me.moonchan.ts.downloader.gui.ui.BaseContract;

public class MainContract {
    interface View extends BaseContract.View {
        void showAddDownloadTaskDialog();
    }

    interface Presenter extends BaseContract.Presenter<View> {
        void shutdownDownloader();
    }
}