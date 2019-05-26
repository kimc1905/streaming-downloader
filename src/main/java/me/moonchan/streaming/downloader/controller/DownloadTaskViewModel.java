package me.moonchan.streaming.downloader.controller;

import javafx.beans.property.*;
import lombok.Getter;
import me.moonchan.streaming.downloader.DownloadTask;
import me.moonchan.streaming.downloader.DownloadUrl;

@Getter
public class DownloadTaskViewModel {

    private DownloadTask downloadTask;

    private StringProperty name;
    private StringProperty url;
    private StringProperty dest;
    private StringProperty state;
    private DoubleProperty progress;

    public DownloadTaskViewModel(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
        DownloadUrl downloadUrl = downloadTask.getDownloadUrl();
        this.name = new SimpleStringProperty(downloadTask.getDest().getName());
        this.url = new SimpleStringProperty(downloadUrl.getUrlFormat());
        this.dest = new SimpleStringProperty(downloadTask.getDest().getAbsolutePath());
        this.state = new SimpleStringProperty("READY");
        this.progress = new SimpleDoubleProperty(0.0);
        downloadTask.setOnDownloadListener((current, length) -> {
            progress.set((double)current / (double)length);
        });
        downloadTask.setOnDownloadStateChangeListener(state -> {
            this.state.set(state.toString());
        });
    }
}
