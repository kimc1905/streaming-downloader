package me.moonchan.ts.downloader.gui.util;

import me.moonchan.ts.downloader.core.Bitrate;
import me.moonchan.ts.downloader.core.Cookie;
import me.moonchan.ts.downloader.core.DownloadInfo;
import me.moonchan.ts.downloader.core.url.DownloadUrl;

import java.util.Optional;
import java.util.prefs.BackingStoreException;

public class AppPreferences {
    private static final String PREF_RECENT_SAVE_FILE = "recentSaveDir";
    private static final String PREF_RECENT_COOKIE = "cookie";
    private static final String PREF_RECENT_START = "recentStart";
    private static final String PREF_RECENT_BITRATE = "bitrate";
    private static final String PREF_MAIN_STAGE_WIDTH = "PREF_MAIN_STAGE_WIDTH";
    private static final String PREF_MAIN_STAGE_HEIGHT = "PREF_MAIN_STAGE_HEIGHT";
    private static final String PREF_MAIN_STAGE_X = "PREF_MAIN_STAGE_X";
    private static final String PREF_MAIN_STAGE_Y = "PREF_MAIN_STAGE_Y";

    private JsonPreferences preferences;

    public AppPreferences() {
        this.preferences = new JsonPreferences();
    }

    public void setMainStageWidth(double width) {
        preferences.putDouble(PREF_MAIN_STAGE_WIDTH, width);
    }

    public double getMainStageWidth(double def) {
        return preferences.getDouble(PREF_MAIN_STAGE_WIDTH, def);
    }

    public void setMainStageHeight(double height) {
        preferences.putDouble(PREF_MAIN_STAGE_HEIGHT, height);
    }

    public double getMainStageHeight(double def) {
        return preferences.getDouble(PREF_MAIN_STAGE_HEIGHT, def);
    }

    public void setMainStageX(double x) {
        preferences.putDouble(PREF_MAIN_STAGE_X, x);
    }

    public double getMainStageX(double def) {
        return preferences.getDouble(PREF_MAIN_STAGE_X, def);
    }

    public void setMainStageY(double y) {
        preferences.putDouble(PREF_MAIN_STAGE_Y, y);
    }

    public double getMainStageY(double def) {
        return preferences.getDouble(PREF_MAIN_STAGE_Y, def);
    }

    public Optional<Cookie> getRecentCookie() {
        return preferences.getObject(PREF_RECENT_COOKIE, Cookie.class);
    }

    public String getRecentSaveFile() {
        return preferences.get(PREF_RECENT_SAVE_FILE, "");
    }

    public int getRecentStart() {
        return preferences.getInt(PREF_RECENT_START, 1);
    }

    public Bitrate getRecentBitrate(Bitrate def) {
        return Bitrate.valueOf(preferences.get(PREF_RECENT_BITRATE, def.toString()));
    }

    public void setDownloadInfo(DownloadInfo downloadInfo) {
        preferences.put(PREF_RECENT_SAVE_FILE, downloadInfo.getDestFile().getAbsolutePath());
//        preferences.putInt(PREF_RECENT_START, downloadInfo.getStart());
        preferences.putObject(PREF_RECENT_COOKIE, downloadInfo.getCookie());
        DownloadUrl downloadUrl = downloadInfo.getDownloadUrl();
        if(downloadUrl.hasBitrate()) {
            downloadUrl.getBitrate().ifPresent(v -> {
                preferences.put(PREF_RECENT_BITRATE, v.toString());
            });
        }
    }

    public void clear() {
        try {
            preferences.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
}
