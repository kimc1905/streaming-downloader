package me.moonchan.streaming.downloader.domain;

import me.moonchan.streaming.downloader.ui.addtask.CookieViewModel;

import java.util.*;
import java.util.stream.Collectors;

public class Cookie {
    private Map<String, String> cookieMap;

    public Cookie() {
        cookieMap = new HashMap<>();
    }

    public void putCookie(String name, String value) {
        cookieMap.put(name, value);
    }

    public List<CookieViewModel> getCookieViewModelList() {
        Set<Map.Entry<String, String>> entries = cookieMap.entrySet();
        List<CookieViewModel> result = new ArrayList<>();
        entries.forEach(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            result.add(new CookieViewModel(key, value));
        });
        Collections.sort(result);
        return result;
    }

    @Override
    public String toString() {
        return cookieMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("; "));
    }
}