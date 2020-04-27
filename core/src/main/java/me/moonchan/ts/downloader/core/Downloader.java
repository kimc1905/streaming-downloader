package me.moonchan.ts.downloader.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Downloader {
    private ExecutorService executorService;

    public Downloader() {
        this(4);
    }

    public Downloader(int nThreads) {
        this.executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void addDownloadTask(DownloadTask task) {
        Thread run = new Thread(() -> task.download());
        executorService.submit(run);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}