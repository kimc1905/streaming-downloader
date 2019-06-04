package me.moonchan.streaming.downloader.ui;

import javafx.scene.input.Clipboard;
import lombok.extern.log4j.Log4j;
import me.moonchan.streaming.downloader.*;
import me.moonchan.streaming.downloader.ui.addtask.AddDownloadTaskView;
import me.moonchan.streaming.downloader.ui.download.DownloadView;
import me.moonchan.streaming.downloader.util.EventBus;
import okhttp3.OkHttpClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j
public class MainViewModel {

    public static final String PREF_RECENT_SAVE_FILE = "recentSaveDir";
    public static final String PREF_COOKIE = "cookie";

    private JsonPreferences preferences;
    private OkHttpClient client;
    private Downloader downloader;
    private DownloadView downloadView;

    public MainViewModel(DownloadView downloadView) {
        this.downloadView = downloadView;
        this.preferences = new JsonPreferences();
        this.client = new OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        this.downloader = new Downloader(5);

        EventBus.get().getObservable(DownloadInfo.class)
                .subscribe(this::onAddDownloadTask);
    }

    private void onAddDownloadTask(DownloadInfo downloadInfo) {
        DownloadTask task = new DownloadTask(client, downloadInfo);
        downloader.addDownloadTask(task);
        downloadView.addDownloadTask(task);

        preferences.put(PREF_RECENT_SAVE_FILE, downloadInfo.getSaveLocation().getAbsolutePath());
        preferences.putObject(PREF_COOKIE, downloadInfo.getCookie());
    }

    public void clearFinishedTask() {
        downloadView.clearFinishedDownloadTask();
    }

    public void initAddDownloadTaskView(AddDownloadTaskView view) {
        // 클립보드에 다운받을 스트리밍 주소가 있으면 미리 설정
        String clipboardText = Clipboard.getSystemClipboard().getString();
        if(clipboardText != null)
            view.setUrl(clipboardText);
        // 최근 쿠키 설정
        Optional<Cookie> cookie = preferences.getObject(PREF_COOKIE, Cookie.class);
        cookie.ifPresent(v -> view.setCookie(v));
        // 최근 저장 위치 설정
        String recentSaveDirPath = preferences.get(PREF_RECENT_SAVE_FILE, "");
        if(!recentSaveDirPath.isEmpty()) {
            view.setRecentSaveFile(recentSaveDirPath);
        }
    }

    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
