package me.moonchan.streaming.downloader.event;

import me.moonchan.streaming.downloader.domain.DownloadInfo;

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