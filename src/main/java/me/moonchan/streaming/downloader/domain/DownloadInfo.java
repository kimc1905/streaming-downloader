package me.moonchan.streaming.downloader.domain;

import lombok.Getter;

import java.io.File;

@Getter
public class DownloadInfo {
    private DownloadUrl downloadUrl;
    private File saveLocation;
    private Cookie cookie;
    private int start;

    public DownloadInfo(DownloadUrl downloadUrl, File saveLocation, Cookie cookie, int start) {
        this.downloadUrl = downloadUrl;
        this.saveLocation = saveLocation;
        this.cookie = cookie;
        this.start = start;
    }
}