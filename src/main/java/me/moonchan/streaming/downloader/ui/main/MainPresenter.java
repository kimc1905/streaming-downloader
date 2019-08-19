package me.moonchan.streaming.downloader.ui.main;

import lombok.extern.slf4j.Slf4j;
import me.moonchan.streaming.downloader.domain.DownloadInfo;
import me.moonchan.streaming.downloader.domain.DownloadTask;
import me.moonchan.streaming.downloader.domain.Downloader;
import me.moonchan.streaming.downloader.event.AddDownloadInfoEvent;
import me.moonchan.streaming.downloader.event.AddDownloadTaskEvent;
import me.moonchan.streaming.downloader.event.ToolbarEvent;
import me.moonchan.streaming.downloader.ui.addtask.AddDownloadTaskContract;
import me.moonchan.streaming.downloader.util.Constants;
import me.moonchan.streaming.downloader.util.EventBus;
import me.moonchan.streaming.downloader.util.JsonPreferences;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MainPresenter implements MainContract.Presenter {
    private JsonPreferences preferences;
    private OkHttpClient client;
    private Downloader downloader;
    private MainContract.View view;
    private EventBus eventBus;

    public MainPresenter(EventBus eventBus) {
        this.preferences = new JsonPreferences();
        this.client = new OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        this.downloader = new Downloader(5);
        this.eventBus = eventBus;
    }

    @PostConstruct
    private void init() {
        this.eventBus.register(ToolbarEvent.class, this::onToolbarEvent);
        this.eventBus.register(AddDownloadInfoEvent.class, this::onAddDownloadTask);
    }

    private void onToolbarEvent(ToolbarEvent event) {
        if (event.isEqualSource(Constants.ComponentId.BTN_ADD_DOWNLOAD_TASK)) {
            view.showAddDownloadTaskDialog();
        }
    }

    private void onAddDownloadTask(AddDownloadInfoEvent event) {
        DownloadInfo downloadInfo = event.getDownloadInfo();
        DownloadTask task = new DownloadTask(client, downloadInfo);
        downloader.addDownloadTask(task);
        eventBus.send(AddDownloadTaskEvent.of(task));
        preferences.put(Constants.PreferenceKey.PREF_RECENT_SAVE_FILE, downloadInfo.getSaveLocation().getAbsolutePath());
        preferences.putInt(Constants.PreferenceKey.PREF_RECENT_START, downloadInfo.getStart());
        preferences.putObject(Constants.PreferenceKey.PREF_RECENT_COOKIE, downloadInfo.getCookie());
        downloadInfo.getDownloadUrl().getBitrate().ifPresent(bitrate -> {
            preferences.putInt(Constants.PreferenceKey.PREF_RECENT_BITRATE, bitrate);
        });
    }

    @Override
    public void shutdownDownloader() {
        downloader.shutdown();
    }

    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void setView(MainContract.View view) {
        this.view = view;
    }
}