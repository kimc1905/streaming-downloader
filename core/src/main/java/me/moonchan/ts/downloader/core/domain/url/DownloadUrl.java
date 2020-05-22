package me.moonchan.ts.downloader.core.domain.url;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.core.domain.model.Bitrate;
import me.moonchan.ts.downloader.core.exception.UrlParseException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Setter
public class DownloadUrl {
    protected String m3u8Url;
    protected String baseUrl;

    public DownloadUrl() {
    }

    public DownloadUrl(String url) {
        if(!url.contains(".m3u8")) {
            throw new UrlParseException(url);
        }
        this.m3u8Url = url;
        this.baseUrl = m3u8Url.substring(0, m3u8Url.lastIndexOf("/") + 1);
    }

    public String getM3u8Url() {
        return m3u8Url;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getContentUrl(String content) {
        return baseUrl + content;
    }

    public Optional<Bitrate> getBitrate() {
        Pattern pattern = Pattern.compile(getBitratePattern());
        Matcher matcher = pattern.matcher(baseUrl);
        if (matcher.find()) {
            Bitrate bitrate = Bitrate.valueOf(Integer.parseInt(matcher.group(1)));
            if(bitrate != null)
                return Optional.of(bitrate);
        }
        return Optional.empty();
    }

    public void setBitrate(Bitrate bitrate) {
    }

    public boolean hasBitrate() {
        return !getBitratePattern().isEmpty();
    }

    protected String getBitratePattern() {
        return "";
    }
}