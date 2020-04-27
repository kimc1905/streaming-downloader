package me.moonchan.ts.downloader.core.url;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.core.Bitrate;
import me.moonchan.ts.downloader.core.exception.UrlParseException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Setter
public class DownloadUrl {
    private static final String PATTERN_TS_URL = "(\\S+)(\\D+)(\\d+)(.ts|.m4s)(\\S*$)";
    protected String urlFormat;
    protected int start;
    protected int end;

    public DownloadUrl() {
    }

    public DownloadUrl(String url) {
        parseUrl(url);
    }

    public void parseUrl(String url) {
        int beginIndex = url.lastIndexOf("/");
        String fileName = url.substring(beginIndex + 1);

        Pattern pattern = Pattern.compile(PATTERN_TS_URL);
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            String format = new StringBuilder()
                    .append(url, 0, beginIndex + 1)
                    .append(matcher.group(1))
                    .append(matcher.group(2))
                    .append("%d")
                    .append(matcher.group(4))
                    .append(matcher.group(5))
                    .toString();
            this.urlFormat = format;
            this.start = 1;
            this.end = Integer.parseInt(matcher.group(3));
        } else {
            throw new UrlParseException(url);
        }
    }

    public String getUrlFormat() {
        return urlFormat;
    }

    public String getUrl(int index) {
        if (index < start || index > end)
            throw new RuntimeException("요청한 인덱스가 범위를 벗어났습니다.");
        return String.format(urlFormat, index);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Optional<Bitrate> getBitrate() {
        Pattern pattern = Pattern.compile(getBitratePattern());
        Matcher matcher = pattern.matcher(urlFormat);
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