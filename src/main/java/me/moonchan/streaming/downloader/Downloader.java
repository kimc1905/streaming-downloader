package me.moonchan.streaming.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Downloader {
    private int nThreads;
    private ExecutorService executorService;
    private List<DownloadTask> downloadTasks;

    public Downloader() {
        this(4);
        downloadTasks = new ArrayList<>();
    }

    public Downloader(int nThreads) {
        this.nThreads = nThreads;
        this.executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void addDownload(DownloadTask task, String lastUrl, int bitrate) {

    }


}
