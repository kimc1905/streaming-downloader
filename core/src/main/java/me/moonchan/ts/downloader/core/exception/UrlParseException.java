package me.moonchan.ts.downloader.core.exception;

public class UrlParseException extends RuntimeException {
    public UrlParseException(String url) {
        super("Can't parse url: " + url);
    }
}
