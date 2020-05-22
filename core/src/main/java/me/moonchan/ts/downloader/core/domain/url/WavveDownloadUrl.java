package me.moonchan.ts.downloader.core.domain.url;

import me.moonchan.ts.downloader.core.domain.model.Bitrate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.moonchan.ts.downloader.core.domain.model.Bitrate.valueOf;

public class WavveDownloadUrl extends DownloadUrl {
    public WavveDownloadUrl(String url) {
        super(url);
    }

    @Override
    public void setBitrate(Bitrate bitrate) {
        Pattern pattern = Pattern.compile(getBitratePattern());
        Matcher matcher = pattern.matcher(baseUrl);
        if (matcher.find()) {
            Bitrate original = valueOf(Integer.parseInt(matcher.group(1)));
            if(original == null)
                return;
            if (original != bitrate) {
                baseUrl = baseUrl.replace("/" + getWavveBitrate(original) + "/", "/" + getWavveBitrate(bitrate) + "/");
            }
        }
    }

    @Override
    protected String getBitratePattern() {
        return "/(\\d{3,4})/";
    }

    private int getWavveBitrate(Bitrate bitrate) {
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
