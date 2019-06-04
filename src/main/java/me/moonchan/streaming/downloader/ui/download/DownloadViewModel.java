package me.moonchan.streaming.downloader.ui.download;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.log4j.Log4j;
import me.moonchan.streaming.downloader.DownloadTask;

@Log4j
public class DownloadViewModel {

    private ObservableList<DownloadTaskViewModel> downloadTasks;

    public DownloadViewModel() {
        downloadTasks = FXCollections.observableArrayList();
    }

    public void addDownloadTask(DownloadTask task) {
        downloadTasks.add(new DownloadTaskViewModel(task));
    }

    public void clearFinishedDownloadTask() {
        downloadTasks.removeIf(downloadTask -> downloadTask.isFinished());
    }

    public ObservableList<DownloadTaskViewModel> getDownloadTasks() {
        return downloadTasks;
    }
}
