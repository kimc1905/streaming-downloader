package me.moonchan.ts.downloader.gui.ui.download;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import me.moonchan.ts.downloader.core.DownloadTask;
import me.moonchan.ts.downloader.core.domain.url.DownloadUrl;

@Getter
public class DownloadTaskViewModel {

    private StringProperty name;
    private StringProperty url;
    private StringProperty dest;
    private StringProperty state;
    private DoubleProperty progress;
    private DownloadTask downloadTask;
    private CompositeDisposable disposable;

    public DownloadTaskViewModel(DownloadTask downloadTask) {
        disposable = new CompositeDisposable();
        DownloadUrl downloadUrl = downloadTask.getDownloadUrl();
        this.downloadTask = downloadTask;
        this.name = new SimpleStringProperty(downloadTask.getDestFile().getName());
        this.url = new SimpleStringProperty(downloadUrl.getM3u8Url());
        this.dest = new SimpleStringProperty(downloadTask.getDestFile().getAbsolutePath());
        this.state = new SimpleStringProperty();
        this.progress = new SimpleDoubleProperty(0.0);
        this.state = new SimpleStringProperty(downloadTask.getState().toString());
        disposable.add(downloadTask.getObservableProgress().subscribe(this::onProgressChanged));
        disposable.add(downloadTask.getObservableState().subscribe(this::onStateChanged));
    }

    public boolean isFinished() {
        return downloadTask.isFinished();
    }

    private void onProgressChanged(DownloadTask.Progress progress) {
        this.progress.set(progress.getPercent());
    }

    private void onStateChanged(DownloadTask.State state) {
        this.state.set(state.toString());
        if(state == DownloadTask.State.COMPLETE) {
            progress.set(1.0);
        }
    }

    public void dispose() {
        if(!disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}