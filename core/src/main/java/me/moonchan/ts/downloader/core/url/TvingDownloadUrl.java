package me.moonchan.ts.downloader.core.url;

import me.moonchan.ts.downloader.core.Bitrate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TvingDownloadUrl extends DownloadUrl {
    public TvingDownloadUrl(String url) {
        super(url);
        super.start = 0;
    }

    @Override
    public void setBitrate(Bitrate bitrate) {
        Pattern pattern = Pattern.compile(getBitratePattern());
        Matcher matcher = pattern.matcher(urlFormat);
        if (matcher.find()) {
            Bitrate original = Bitrate.valueOf(Integer.parseInt(matcher.group(1)));
            if(original == null)
                return;
            if (original != bitrate) {
                urlFormat = urlFormat.replace(getTvingBitrateString(original),
                        getTvingBitrateString(bitrate));
                Pattern secPattern = Pattern.compile(getSecBitratePattern());
                Matcher secMatcher = secPattern.matcher(urlFormat);
                if(secMatcher.find()) {
                    urlFormat = urlFormat.replace(secMatcher.group(1),
                            String.valueOf(getTvingSecBitrate(bitrate)));
                }
            }
        }
    }

    @Override
    protected String getBitratePattern() {
        return "_t(\\d{2}).";
    }

    private String getSecBitratePattern() {
        return "content_(510000|1400000|2700000|5500000)_";
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
