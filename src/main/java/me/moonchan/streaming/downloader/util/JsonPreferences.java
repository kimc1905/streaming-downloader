package me.moonchan.streaming.downloader.util;

import com.google.gson.Gson;

import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class JsonPreferences {

    private Preferences preferences;
    private Gson gson;

    public JsonPreferences() {
        preferences = Preferences.userNodeForPackage(this.getClass());
        gson = new Gson();
    }

    public void put(String key, String value) {
        preferences.put(key, value);
    }

    public void putInt(String key, int value) {
        preferences.putInt(key, value);
    }

    public void putObject(String key, Object o) {
        String s = gson.toJson(o);
        preferences.put(key, s);
    }

    public String get(String key, String def) {
        return preferences.get(key, def);
    }

    public int getInt(String key, int def) {
        return preferences.getInt(key, def);
    }

    public <T> Optional<T> getObject(String key, Class<T> tClass) {
        String json = preferences.get(key, "");
        if (json.isEmpty())
            return Optional.empty();
        T value = gson.fromJson(json, tClass);
        if (value == null)
            return Optional.empty();
        return Optional.of(value);
    }

    public void clear() throws BackingStoreException {
        preferences.clear();
    }
}