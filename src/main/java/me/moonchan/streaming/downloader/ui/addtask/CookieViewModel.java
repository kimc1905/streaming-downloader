package me.moonchan.streaming.downloader.ui.addtask;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CookieViewModel implements Comparable<CookieViewModel> {
    private StringProperty key;
    private StringProperty value;

    public CookieViewModel(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public boolean isSameKey(String key) {
        return this.key.getValue().equals(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CookieViewModel)) return false;
        CookieViewModel that = (CookieViewModel) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public int compareTo(CookieViewModel o) {
        if (this == o) return 1;
        return key.get().compareTo(o.getKey().get());
    }
}
