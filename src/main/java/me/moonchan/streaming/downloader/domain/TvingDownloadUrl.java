package me.moonchan.streaming.downloader.domain;

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
            }
        }
    }

    @Override
    protected String getBitratePattern() {
        return "_t(\\d{2}).";
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
}
