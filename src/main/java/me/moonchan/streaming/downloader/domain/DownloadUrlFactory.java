package me.moonchan.streaming.downloader.domain;

public class DownloadUrlFactory {
    public static DownloadUrl create(String url) {
        if(url.contains("pooq.co.kr")) {
            return new PooqDownloadUrl(url);
        }
        if(url.contains("tving.com")) {
            return new TvingDownloadUrl(url);
        }
        return new DownloadUrl(url);
    }
}
