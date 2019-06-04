package me.moonchan.streaming.downloader;

import lombok.Getter;

import java.io.File;

@Getter
public class DownloadInfo {
    DownloadUrl downloadUrl;
    File saveLocation;
    Cookie cookie;

    public DownloadInfo(DownloadUrl downloadUrl, File saveLocation, Cookie cookie) {
        this.downloadUrl = downloadUrl;
        this.saveLocation = saveLocation;
        this.cookie = cookie;
    }
}
