package me.moonchan.ts.downloader.core;

import lombok.Getter;
import me.moonchan.ts.downloader.core.domain.model.Cookie;
import me.moonchan.ts.downloader.core.domain.url.DownloadUrl;
import okhttp3.OkHttpClient;

import java.io.File;

@Getter
public class DownloadInfo {
    private DownloadUrl downloadUrl;
    private File destFile;
    private Cookie cookie;

    public DownloadInfo(DownloadUrl downloadUrl, File destFile, Cookie cookie) {
        this.downloadUrl = downloadUrl;
        this.destFile = destFile;
        this.cookie = cookie;
    }

    public DownloadTask toDownloadTask(OkHttpClient client) {
        return new DownloadTask(client, this);
    }

    public String getDestDir() {
        String destPath = destFile.getPath().replaceAll("\\\\", "/");
        return destPath.substring(0, destPath.lastIndexOf("/"));
    }
}