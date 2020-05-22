package me.moonchan.ts.downloader.gui.util;

import me.moonchan.ts.downloader.core.domain.model.Cookie;
import me.moonchan.ts.downloader.gui.ui.addtask.CookieViewModel;

import java.util.*;

public class CookieUtil {
    public static List<CookieViewModel> getCookieViewModelList(Cookie cookie) {
        Set<Map.Entry<String, String>> entries = cookie.getCookieMap().entrySet();
        List<CookieViewModel> result = new ArrayList<>();
        entries.forEach(entry -> {
            String key = entry.getKey();
            String value = entry.getValue();
            result.add(new CookieViewModel(key, value));
        });
        Collections.sort(result);
        return result;
    }
}
