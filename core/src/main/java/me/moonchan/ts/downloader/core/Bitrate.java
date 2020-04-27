package me.moonchan.ts.downloader.core;

public enum  Bitrate {
    MOBILE, SD, HD, FHD;

    public static Bitrate valueOf(int bitrate) {
        switch (bitrate) {
            case 31:
            case 500:
                return MOBILE;
            case 33:
            case 1000:
                return SD;
            case 34:
            case 2000:
                return HD;
            case 35:
            case 5000:
                return FHD;
        }
        return null;
    }
}
