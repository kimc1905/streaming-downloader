package me.moonchan.ts.downloader.gui.ui.main;

import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.core.DownloadInfo;
import me.moonchan.ts.downloader.core.DownloadTask;
import me.moonchan.ts.downloader.core.Downloader;
import me.moonchan.ts.downloader.gui.event.AddDownloadInfoEvent;
import me.moonchan.ts.downloader.gui.event.AddDownloadTaskEvent;
import me.moonchan.ts.downloader.gui.event.ToolbarEvent;
import me.moonchan.ts.downloader.gui.util.AppPreferences;
import me.moonchan.ts.downloader.gui.util.Constants;
import me.moonchan.ts.downloader.gui.util.EventBus;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MainPresenter implements MainContract.Presenter {
    private AppPreferences preferences;
    private OkHttpClient client;
    private Downloader downloader;
    private MainContract.View view;
    private EventBus eventBus;

    @Autowired
    public MainPresenter(EventBus eventBus, AppPreferences preferences, OkHttpClient client) {
        this.preferences = preferences;
        this.client = client;
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
        DownloadTask task = downloadInfo.toDownloadTask(client);
        downloader.addDownloadTask(task);
        eventBus.send(AddDownloadTaskEvent.of(task));
        setPreferences(downloadInfo);
    }

    private void setPreferences(DownloadInfo downloadInfo) {
        preferences.setDownloadInfo(downloadInfo);
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