package me.moonchan.streaming.downloader.domain;

public class NaverDownloadUrl extends DownloadUrl {

    public NaverDownloadUrl(String url) {
        super(url);
    }

    @Override
    public void parseUrl(String url) {
        super.parseUrl(url);
        this.urlFormat = urlFormat.replace("%d", "%06d");
    }
}
