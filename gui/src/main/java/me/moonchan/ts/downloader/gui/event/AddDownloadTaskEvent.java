package me.moonchan.ts.downloader.gui.event;

import me.moonchan.ts.downloader.core.DownloadTask;

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