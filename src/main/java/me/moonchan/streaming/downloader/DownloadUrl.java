package me.moonchan.streaming.downloader;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DownloadUrl {
    public static final String PATTERN_TS_URL = "(\\S+)(\\D+)(\\d+)(.ts)(\\S*$)";
    public static final String PATTERN_BITRATE = "/(\\d{4})/";
    private String urlFormat;
    private int start;
    private int end;

    private DownloadUrl(String urlFormat, int end) {
        this(urlFormat, 1, end);
    }

    private DownloadUrl(String urlFormat, int start, int end) {
        this.urlFormat = urlFormat;
        this.start = start;
        this.end = end;
    }

    public static DownloadUrl of(String urlFormat, int start, int end) {
        return new DownloadUrl(urlFormat, start, end);
    }

    public static DownloadUrl of(String urlFormat, int start, int end, int bitrate) {
        Pattern pattern = Pattern.compile(PATTERN_BITRATE);
        Matcher matcher = pattern.matcher(urlFormat);
        String newUrlFormat = urlFormat;

        if (matcher.find()) {
            int originalBitrate = Integer.parseInt(matcher.group(1));
            if (originalBitrate != bitrate) {
                newUrlFormat = urlFormat.replace("/" + originalBitrate + "/", "/" + bitrate + "/");
            }
        }
        return of(newUrlFormat, start, end);
    }

    public static DownloadUrl of(String url) {
        int beginIndex = url.lastIndexOf("/");
        String fileName = url.substring(beginIndex + 1);

        Pattern pattern = Pattern.compile(PATTERN_TS_URL);
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            String format = new StringBuilder()
                    .append(url, 0, beginIndex)
                    .append("/" + matcher.group(1) + matcher.group(2))
                    .append("%d")
                    .append(matcher.group(4))
                    .append(matcher.group(5))
                    .toString();

            return new DownloadUrl(format, Integer.parseInt(matcher.group(3)));
        }
        throw new RuntimeException("해당 url을 변환할 수 없습니다. " + url);
    }

    public String getUrlFormat() {
        return urlFormat;
    }

    public String getUrl(int index) {
        if(index < start || index > end)
            throw new RuntimeException("요청한 인덱스가 범위를 벗어났습니다.");
        return String.format(urlFormat, index);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
