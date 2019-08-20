package me.moonchan.streaming.downloader.exception;

public class UrlParseException extends RuntimeException {
    private String url;
    public UrlParseException(String url) {
        super("Can't parse url: " + url);
    }
}
