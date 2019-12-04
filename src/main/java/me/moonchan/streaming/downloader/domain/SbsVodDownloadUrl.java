package me.moonchan.streaming.downloader.domain;

public class SbsVodDownloadUrl extends DownloadUrl {

    public SbsVodDownloadUrl(String url) {
        super(url);
        this.start = 0;
    }
}
