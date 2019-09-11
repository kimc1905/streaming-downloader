package me.moonchan.streaming.downloader.domain;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PooqDownloadUrl extends DownloadUrl {
    public PooqDownloadUrl(String url) {
        super(url);
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
                urlFormat = urlFormat.replace("/" + getPooqBitrate(original) + "/", "/" + getPooqBitrate(bitrate) + "/");
            }
        }
    }

    @Override
    protected String getBitratePattern() {
        return "/(\\d{3,4})/";
    }

    private int getPooqBitrate(Bitrate bitrate) {
        switch (bitrate) {
            case MOBILE:
                return 500;
            case SD:
                return 1000;
            case HD:
                return 2000;
            case FHD:
                return 5000;
        }
        return -1;
    }
}
