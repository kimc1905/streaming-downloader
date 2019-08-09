package me.moonchan.streaming.downloader.event;

import me.moonchan.streaming.downloader.domain.DownloadTask;

public class AddDownloadTaskEvent {
    private DownloadTask task;

    private AddDownloadTaskEvent(DownloadTask task) {
        this.task = task;
    }

    public static AddDownloadTaskEvent of(DownloadTask task) {
        return new AddDownloadTaskEvent(task);
    }

    public DownloadTask getDownloadTask() {
        return task;
    }
}
