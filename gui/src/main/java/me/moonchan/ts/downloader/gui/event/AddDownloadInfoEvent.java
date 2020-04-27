package me.moonchan.ts.downloader.gui.event;

import me.moonchan.ts.downloader.core.DownloadInfo;

public class AddDownloadInfoEvent {
    DownloadInfo downloadInfo;

    private AddDownloadInfoEvent(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    public static AddDownloadInfoEvent of(DownloadInfo downloadInfo) {
        return new AddDownloadInfoEvent(downloadInfo);
    }

    public DownloadInfo getDownloadInfo() {
        return downloadInfo;
    }
}