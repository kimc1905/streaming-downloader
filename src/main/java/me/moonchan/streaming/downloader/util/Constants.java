package me.moonchan.streaming.downloader.util;

public final class Constants {

    private Constants() {

    }

    public static class EventMessage {
        public static final String APPLICATION_STOP = "Application Stop";
    }
    public static class ComponentId {
        public static final String BTN_ADD_DOWNLOAD_TASK = "btnAddDownloadTask";
        public static final String BTN_CLEAR_FINISHED_TASK = "btnClearFinishedTask";
    }

    public static class PreferenceKey {
        public static final String PREF_RECENT_SAVE_FILE = "recentSaveDir";
        public static final String PREF_RECENT_COOKIE = "cookie";
        public static final String PREF_RECENT_START = "recentStart";
        public static final String PREF_MAIN_STAGE_WIDTH = "PREF_MAIN_STAGE_WIDTH";
        public static final String PREF_MAIN_STAGE_HEIGHT = "PREF_MAIN_STAGE_HEIGHT";
        public static final String PREF_MAIN_STAGE_X = "PREF_MAIN_STAGE_X";
        public static final String PREF_MAIN_STAGE_Y = "PREF_MAIN_STAGE_Y";
    }
}
