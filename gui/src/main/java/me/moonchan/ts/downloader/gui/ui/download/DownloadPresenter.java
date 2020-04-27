package me.moonchan.ts.downloader.gui.ui.download;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import lombok.extern.slf4j.Slf4j;
import me.moonchan.ts.downloader.gui.event.AddDownloadTaskEvent;
import me.moonchan.ts.downloader.gui.event.ToolbarEvent;
import me.moonchan.ts.downloader.gui.util.Constants;
import me.moonchan.ts.downloader.gui.util.EventBus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class DownloadPresenter implements DownloadContract.Presenter {

    private ObservableList<DownloadTaskViewModel> downloadTasks;
    private EventBus eventBus;
    private DownloadContract.View view;

    public DownloadPresenter(EventBus eventBus) {
        this.downloadTasks = FXCollections.observableArrayList();
        this.eventBus = eventBus;
    }

    @PostConstruct
    private void init() {
        eventBus.register(AddDownloadTaskEvent.class, this::addDownloadTask);
        eventBus.register(ToolbarEvent.class, this::onToolbarEvent);
    }

    private void addDownloadTask(AddDownloadTaskEvent event) {
        downloadTasks.add(new DownloadTaskViewModel(event.getDownloadTask()));
    }

    private void onToolbarEvent(ToolbarEvent event) {
        if (event.isEqualSource(Constants.ComponentId.BTN_CLEAR_FINISHED_TASK)) {
            downloadTasks.removeIf(DownloadTaskViewModel::isFinished);
        }
    }

    @Override
    public void setView(DownloadContract.View view) {
        this.view = view;
    }

    @Override
    public void bindDownloadTaskTable(TableView<DownloadTaskViewModel> tableDownloadTask) {
        tableDownloadTask.setItems(downloadTasks);
    }
}