package me.moonchan.streaming.downloader.domain;

import lombok.Getter;
import okhttp3.OkHttpClient;

import java.io.File;

@Getter
public class DownloadInfo {
    private DownloadUrl downloadUrl;
    private File saveLocation;
    private Cookie cookie;

    public DownloadInfo(DownloadUrl downloadUrl, File saveLocation, Cookie cookie) {
        this.downloadUrl = downloadUrl;
        this.saveLocation = saveLocation;
        this.cookie = cookie;
    }

    public int getStart() {
        return downloadUrl.getStart();
    }

    public DownloadTask toDownloadTask(OkHttpClient client) {
        return new DownloadTask(client, this);
    }
}