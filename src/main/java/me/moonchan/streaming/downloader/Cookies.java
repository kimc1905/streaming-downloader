package me.moonchan.streaming.downloader;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cookies {
    private String url;
    private Map<String, String> cookieMap;

    public Cookies(String url) {
        this.url = url;
        cookieMap = new HashMap<>();
    }

    public void putCookie(String name, String value) {
        cookieMap.put(name, value);
    }

    public String getUrl() {
        return url;
    }

    public String toString() {
        return cookieMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" +entry.getValue())
                .collect(Collectors.joining("; "));
    }
}
