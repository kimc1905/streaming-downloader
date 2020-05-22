package me.moonchan.ts.downloader.core.domain.url;

public class DownloadUrlFactory {
    public static DownloadUrl create(String url) {
        if(url.contains("wavve.com")) {
            return new WavveDownloadUrl(url);
        }
        if(url.contains("tving.com")) {
            return new TvingDownloadUrl(url);
        }
        if(url.contains("naver-vod")) {
            return new NaverDownloadUrl(url);
        }
        if(url.contains("sbsmedia/")) {
            return new SbsVodDownloadUrl(url);
        }
        return new DownloadUrl(url);
    }
}
