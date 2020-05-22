package me.moonchan.ts.downloader.core.domain.url;

import me.moonchan.ts.downloader.core.domain.model.Bitrate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TvingDownloadUrl extends DownloadUrl {

    private Bitrate currentBitrate;

    public TvingDownloadUrl(String url) {
        super(url);
    }

    @Override
    public void setBitrate(Bitrate bitrate) {
        Pattern pattern = Pattern.compile(getBitratePattern());
        Matcher matcher = pattern.matcher(baseUrl);
        if (matcher.find()) {
            currentBitrate = bitrate;
            Bitrate original = Bitrate.valueOf(Integer.parseInt(matcher.group(1)));
            if(original == null)
                return;
            if (original != bitrate) {
                baseUrl = baseUrl.replace(getTvingBitrateString(original),
                        getTvingBitrateString(bitrate));
                Pattern secPattern = Pattern.compile(getSecBitratePattern());
                Matcher secMatcher = secPattern.matcher(baseUrl);
                if(secMatcher.find()) {
                    baseUrl = baseUrl.replace(secMatcher.group(1),
                            String.valueOf(getTvingSecBitrate(bitrate)));
                }
            }
        }
    }

    public String getContentUrl(String content) {
        Pattern pattern = Pattern.compile(getSecBitratePattern());
        Matcher secMatcher = pattern.matcher(content);
        if(secMatcher.find()) {
            content = content.replace(secMatcher.group(1),
                    String.valueOf(getTvingSecBitrate(currentBitrate)));
        }
        return baseUrl + content;
    }

    @Override
    protected String getBitratePattern() {
        return "_t(\\d{2}).";
    }

    private String getSecBitratePattern() {
        return "content_(510000|1400000|2700000|5500000)";
    }

    private String getTvingBitrateString(Bitrate bitrate) {
        return   "_t" + getTvingBitrate(bitrate) + ".";
    }

    private int getTvingBitrate(Bitrate bitrate) {
        switch (bitrate) {
            case MOBILE:
                return 31;
            case SD:
                return 33;
            case HD:
                return 34;
            case FHD:
                return 35;
        }
        return -1;
    }

    private int getTvingSecBitrate(Bitrate bitrate) {
        switch (bitrate) {
            case MOBILE:
                return 510000;
            case SD:
                return 1400000;
            case HD:
                return 2700000;
            case FHD:
                return 5500000;
        }
        return -1;
    }
}
