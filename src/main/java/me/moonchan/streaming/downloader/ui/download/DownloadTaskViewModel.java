package me.moonchan.streaming.downloader.ui.download;

import javafx.beans.property.*;
import lombok.Getter;
import me.moonchan.streaming.downloader.DownloadTask;
import me.moonchan.streaming.downloader.DownloadUrl;

@Getter
public class DownloadTaskViewModel {

    private StringProperty name;
    private StringProperty url;
    private StringProperty dest;
    private StringProperty state;
    private DoubleProperty progress;
    private DownloadTask downloadTask;

    public DownloadTaskViewModel(DownloadTask downloadTask) {
        this.downloadTask = downloadTask;
        DownloadUrl downloadUrl = downloadTask.getDownloadUrl();
        this.name = new SimpleStringProperty(downloadTask.getSaveLocation().getName());
        this.url = new SimpleStringProperty(downloadUrl.getUrlFormat());
        this.dest = new SimpleStringProperty(downloadTask.getSaveLocation().getAbsolutePath());
        this.state = new SimpleStringProperty(downloadTask.INIT_STATE.toString());
        this.progress = new SimpleDoubleProperty(0.0);
        downloadTask.getObservableProgress().subscribe(this::onProgressChanged);
        downloadTask.getObservableState().subscribe(this::onStateChanged);
    }

    public boolean isFinished() {
        return downloadTask.isFinished();
    }

    private void onProgressChanged(DownloadTask.Progress progress) {
        this.progress.set(progress.getPercent());
    }

    private void onStateChanged(DownloadTask.State state) {
        this.state.set(state.toString());
    }
}