package me.moonchan.ts.downloader.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cookie {
    private Map<String, String> cookieMap;

    public Cookie() {
        cookieMap = new HashMap<>();
    }

    public void putCookie(String name, String value) {
        cookieMap.put(name, value);
    }

    public Map<String, String> getCookieMap() {
        return Collections.unmodifiableMap(cookieMap);
    }

    @Override
    public String toString() {
        return cookieMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("; "));
    }
}